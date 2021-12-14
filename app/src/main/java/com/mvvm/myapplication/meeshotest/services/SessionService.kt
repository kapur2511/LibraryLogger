package com.mvvm.myapplication.meeshotest.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.mvvm.myapplication.meeshotest.notification.NotificationUtils


class SessionService: Service() {

    companion object{
        private const val SESSION_FOREGROUND_SERVICE_ID = 1001
    }

    //Not a bound service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent!!.getStringExtra("inputExtra")
        val notification = NotificationUtils.createNotification(context = this)
        startForeground(SESSION_FOREGROUND_SERVICE_ID, notification)
        return START_STICKY
    }

}