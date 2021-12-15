package com.mvvm.myapplication.meeshotest

import com.mvvm.myapplication.meeshotest.notification.NotificationUtils
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class LibraryApplication: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannel(context = this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }
}