package com.mvvm.myapplication.meeshotest.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.*
import androidx.core.content.ContextCompat
import com.mvvm.myapplication.meeshotest.notification.NotificationUtils
import com.mvvm.myapplication.meeshotest.ui.SessionActivity.Companion.SESSION_RECEIVER
import com.mvvm.myapplication.meeshotest.ui.SessionActivity.Companion.SESSION_START_TIMESTAMP
import com.mvvm.myapplication.meeshotest.ui.SessionActivity.Companion.SESSION_TIME_ELAPSED
import com.mvvm.myapplication.meeshotest.ui.SessionActivity.Companion.SESSION_TIME_ELAPSED_RESULT_CODE
import java.util.*


class SessionService: Service() {

    companion object{
        private const val SESSION_FOREGROUND_SERVICE_ID = 1001
    }

    private var secondsElapsed = 0
    private var timerRunning = false
    private var receiver: ResultReceiver? = null
    private val handler = Handler(Looper.getMainLooper())

    //Not a bound service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        timerRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null) {
            stopSelf()
            return START_NOT_STICKY
        }
        val startTimeStamp = intent.getLongExtra(SESSION_START_TIMESTAMP, -1)
        receiver = intent.getParcelableExtra<ResultReceiver>(SESSION_RECEIVER)
        //resetting the seconds elapsed before starting timer again.
        secondsElapsed = 0
        handler.removeCallbacksAndMessages(null)
        startTimer(startTimeStamp)

        val notification = NotificationUtils.createNotification(context = this)
        startForeground(SESSION_FOREGROUND_SERVICE_ID, notification)
        return START_STICKY
    }

    private fun startTimer(startTimeStamp: Long) {
        // Call the post() method, passing in a new Runnable.
        // The post() method processes code without a delay,
        // so the code in the Runnable will run almost immediately.
        // Call the post() method, passing in a new Runnable.
        // The post() method processes code without a delay,
        // so the code in the Runnable will run almost immediately.
        val timeElapsedSinceStart = System.currentTimeMillis() - startTimeStamp
        if(timeElapsedSinceStart>1000) {
            secondsElapsed += (timeElapsedSinceStart/1000).toInt()
        }

        handler.post(object : Runnable {
            override fun run() {
                if(timerRunning) {
                    val hours: Int = secondsElapsed / 3600
                    val minutes: Int = secondsElapsed % 3600 / 60
                    val secs: Int = secondsElapsed % 60

                    // Format the seconds into hours, minutes,
                    // and seconds.
                    val time: String = java.lang.String
                        .format(
                            Locale.getDefault(),
                            "%d:%02d:%02d", hours,
                            minutes, secs
                        )

                    updateUI(timeElapsed = time)

                    secondsElapsed++

                    // Post the runnable again
                    // with a delay of 1 second.
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        timerRunning = false
        handler.removeCallbacksAndMessages(null)
    }

    fun updateUI(timeElapsed: String) {
        //update notification
        val manager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        )
        val notification = NotificationUtils.createNotification(this, timeElapsed)
        manager?.notify(SESSION_FOREGROUND_SERVICE_ID, notification)

        //update activity UI if possible
        val data = Bundle()
        data.putString(SESSION_TIME_ELAPSED, timeElapsed)
        receiver?.send(SESSION_TIME_ELAPSED_RESULT_CODE, data)
    }

}