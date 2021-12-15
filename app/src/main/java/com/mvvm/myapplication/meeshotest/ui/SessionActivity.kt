package com.mvvm.myapplication.meeshotest.ui

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
import javax.inject.Inject

class SessionActivity : AppCompatActivity(), AppReceiver {


    companion object {
        const val SESSION_START_TIMESTAMP = "start_timestamp"
        const val SESSION_RECEIVER = "session_receiver"
        const val SESSION_TIME_ELAPSED = "session_time_elapsed"
        const val SESSION_TIME_ELAPSED_RESULT_CODE = 101
    }
    private lateinit var sessionActivityBinding: SessionActivityBinding
    private lateinit var viewModel: SessionViewModel
    private val receiver = CustomResultReceiver(Handler(Looper.getMainLooper()), this)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
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
                        "No active session, click on start session to begin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Loading -> {

                }
            }
        })

        viewModel.setupUiIfSessionIsRunning()
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
