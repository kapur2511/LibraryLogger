package com.mvvm.myapplication.meeshotest.utils

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class CustomResultReceiver(
    handler: Handler,
    var appReceiver: AppReceiver?
): ResultReceiver(handler) {

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        super.onReceiveResult(resultCode, resultData)
        appReceiver?.onReceiveResult(resultCode, resultData)
    }
}

interface AppReceiver {
    fun onReceiveResult(resultCode: Int, resultData: Bundle?)
}