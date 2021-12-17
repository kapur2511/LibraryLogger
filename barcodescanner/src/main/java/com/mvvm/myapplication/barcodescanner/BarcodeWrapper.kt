package com.mvvm.myapplication.barcodescanner

import android.app.Activity
import android.content.Intent
import com.blikoon.qrcodescanner.QrCodeActivity

object BarcodeWrapper {

    const val BARCODE_SUCCESS_KEY = "com.blikoon.qrcodescanner.got_qr_scan_relult"
    const val BARCODE_FAILURE_KEY = "com.blikoon.qrcodescanner.error_decoding_image"

    fun startBarcodeScanner(
        requestCode: Int,
        activity: Activity,
    ) {
        val intent = Intent(activity, QrCodeActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }
}