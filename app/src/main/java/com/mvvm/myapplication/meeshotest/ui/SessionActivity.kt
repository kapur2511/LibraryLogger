package com.mvvm.myapplication.meeshotest.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.mvvm.myapplication.barcodescanner.BarcodeWrapper
import com.mvvm.myapplication.barcodescanner.BarcodeWrapper.BARCODE_SUCCESS_KEY
import com.mvvm.myapplication.meeshotest.data.localmodels.EndSessionDisplayModel
import com.mvvm.myapplication.meeshotest.databinding.SessionActivityBinding
import com.mvvm.myapplication.meeshotest.services.SessionService
import com.mvvm.myapplication.meeshotest.utils.*
import com.mvvm.myapplication.meeshotest.viewmodel.SessionViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject


class SessionActivity : AppCompatActivity(), AppReceiver {


    companion object {
        const val SESSION_START_TIMESTAMP = "start_timestamp"
        const val SESSION_RECEIVER = "session_receiver"
        const val SESSION_TIME_ELAPSED = "session_time_elapsed"
        const val SESSION_TIME_ELAPSED_RESULT_CODE = 101
        const val BARCODE_START_SESSION_REQUEST_CODE = 102
        const val BARCODE_END_SESSION_REQUEST_CODE = 103
        const val PERMISSION_REQUESTS = 104
    }
    private lateinit var sessionActivityBinding: SessionActivityBinding
    private lateinit var viewModel: SessionViewModel
    private val receiver = CustomResultReceiver(Handler(Looper.getMainLooper()), this)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        sessionActivityBinding = SessionActivityBinding.inflate(layoutInflater)
        setContentView(sessionActivityBinding.root)
        viewModel = ViewModelProvider(this.viewModelStore, viewModelFactory)[SessionViewModel::class.java]
        setupUI()

        if(PermissionsManager.allPermissionsGranted(this).not()) {
            PermissionsManager.getRuntimePermissions(this)
        }
    }

    private fun setupUI() {
        setupSessionUiObserver()
        setupErrorAlertObserver()
        viewModel.setupUiIfSessionIsRunning()
        setupClickListeners()
    }

    private fun setupSessionUiObserver() {
        viewModel.sessionUIObservable.observe(this, {  result ->
            when(result) {
                is Success -> {
                    sessionActivityBinding.apply {
                        startSessionButton.visibility = View.GONE
                        endSessionButton.visibility = View.VISIBLE
                        val intent = Intent(this@SessionActivity, SessionService::class.java)
                        intent.putExtra(SESSION_START_TIMESTAMP, result.data.startTimestamp)
                        intent.putExtra(SESSION_RECEIVER, receiver)
                        ContextCompat.startForegroundService(
                            this@SessionActivity,
                            intent
                        )
                    }
                }
                is Error -> {
                    sessionActivityBinding.apply {
                        startSessionButton.visibility = View.VISIBLE
                        endSessionButton.visibility = View.GONE
                    }
                    Toast.makeText(
                        this@SessionActivity,
                        result.throwable?.message ?: "Something went wrong, please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Loading -> {
                    /*no-op*/
                }
            }
        })
    }

    private fun setupErrorAlertObserver() {
        viewModel.endSessionAlertObservable.observe(this, { model ->
            if(model.errorString == null) {
                //No error in ending session. Let's stop service and timer.
                val intent = Intent(this@SessionActivity, SessionService::class.java)
                stopService(intent)
                receiver.appReceiver = null
                sessionActivityBinding.tvTimer.text = ""
            }
            showEndSessionAlertDialog(model)
        })
    }

    private fun setupClickListeners() {
        sessionActivityBinding.apply {
            startSessionButton.clickWithThrottle {
                if(PermissionsManager.allPermissionsGranted(this@SessionActivity)) {
                    BarcodeWrapper.startBarcodeScanner(
                        requestCode = BARCODE_START_SESSION_REQUEST_CODE,
                        activity = this@SessionActivity
                    )
                } else {
                    PermissionsManager.showPermissionDialog(this@SessionActivity)
                }
            }

            endSessionButton.clickWithThrottle {
                if(PermissionsManager.allPermissionsGranted(this@SessionActivity)) {
                    BarcodeWrapper.startBarcodeScanner(
                        requestCode = BARCODE_END_SESSION_REQUEST_CODE,
                        activity = this@SessionActivity
                    )
                } else {
                    PermissionsManager.showPermissionDialog(this@SessionActivity)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == BARCODE_START_SESSION_REQUEST_CODE){
            val barcodeData: String? = data?.getStringExtra(BARCODE_SUCCESS_KEY)
            if(barcodeData.isNullOrEmpty().not()) {
                viewModel.checkAndStartSession(barcodeString = barcodeData!!)
            } else {
                Toast.makeText(
                    this@SessionActivity,
                    "Couldn't scan barcode, please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if(resultCode == Activity.RESULT_OK && requestCode == BARCODE_END_SESSION_REQUEST_CODE) {
            val barcodeData: String = data?.getStringExtra(BARCODE_SUCCESS_KEY) ?: ""
            viewModel.endSession(barcodeData)
        } else {
            Toast.makeText(
                this@SessionActivity,
                "Something went wrong, please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showEndSessionAlertDialog(endSessionDisplayModel: EndSessionDisplayModel) {
        val alertDialog = AlertDialog.Builder(this)
        if(endSessionDisplayModel.errorString == null) {
            alertDialog.setTitle("Session ended.")
            alertDialog.setMessage(
                "Your session has ended. Please pay ${endSessionDisplayModel.amountToPay}\n" +
                        "Your session duration was: ${endSessionDisplayModel.timeSpent}\n" +
                        "Your session ended at : ${endSessionDisplayModel.endTime}")
        } else {
            alertDialog.setTitle(endSessionDisplayModel.errorString)
        }
        alertDialog.setPositiveButton("OK"
        ) { dialog, _ ->
            //resets the UI
            viewModel.setupUiIfSessionIsRunning()
            //dismiss the dialog
            dialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        //Callback from service. Update textview with elapsed time
        sessionActivityBinding.tvTimer.text = "Time Elapsed: ${resultData?.getString(SESSION_TIME_ELAPSED)}"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (PermissionsManager.allPermissionsGranted(this).not()) {
            PermissionsManager.showPermissionDialog(this)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.appReceiver = null
    }
}
