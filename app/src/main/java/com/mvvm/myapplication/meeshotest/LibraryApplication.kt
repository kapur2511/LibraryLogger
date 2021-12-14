package com.mvvm.myapplication.meeshotest

import android.app.Application
import com.mvvm.myapplication.meeshotest.notification.NotificationUtils

class LibraryApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannel(context = this)
    }
}