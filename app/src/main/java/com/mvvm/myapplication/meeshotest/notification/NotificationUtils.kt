package com.mvvm.myapplication.meeshotest.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mvvm.myapplication.meeshotest.R
import com.mvvm.myapplication.meeshotest.ui.SessionActivity

object NotificationUtils {

    const val CHANNEL_ID = "session_channel_id"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    fun createNotification(context: Context, timeElapsed: String = "00:00:00"): Notification {
        val notificationIntent = Intent(context, SessionActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent, 0
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Active session")
            .setContentText("Time elapsed: $timeElapsed")
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .build()
    }
}