package com.mvvm.myapplication.meeshotest

import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData

object SessionStub {

    val barcodeData = BarcodeData(
        locationId = "123",
        locationDetail = "456 abc 4th street",
        pricePerMin = 5f,
        startTimestamp = System.currentTimeMillis()
    )
}