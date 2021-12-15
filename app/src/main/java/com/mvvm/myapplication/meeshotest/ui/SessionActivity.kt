package com.mvvm.myapplication.meeshotest.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
        const val BARCODE_REQUEST_CODE = 102
        const val BARCODE_DATA_STRING = "barcode_data_string"
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

                }
            }
        })

        viewModel.setupUiIfSessionIsRunning()

        sessionActivityBinding.apply {
            startSessionButton.clickWithThrottle {
                val intent = Intent(this@SessionActivity, SessionService::class.java)
                startActivityForResult(intent, BARCODE_REQUEST_CODE)
            }

            endSessionButton.clickWithThrottle {
                viewModel.endSession()
                val intent = Intent(this@SessionActivity, SessionService::class.java)
                stopService(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == BARCODE_REQUEST_CODE){
            val barcodeData = data?.getStringExtra(BARCODE_DATA_STRING)
            if(barcodeData.isNullOrEmpty().not()) {
                viewModel.checkAndStartSession(barcodeString = barcodeData!!)
            } else {
                Toast.makeText(
                    this@SessionActivity,
                    "Something went wrong, please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        //Callback from service. Update textview with elapsed time
        sessionActivityBinding.tvTimer.text = resultData?.getString(SESSION_TIME_ELAPSED)
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.appReceiver = null
    }
}
