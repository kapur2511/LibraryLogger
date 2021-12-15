package com.mvvm.myapplication.meeshotest.data.localmodels

import com.google.gson.annotations.SerializedName

data class BarcodeData(
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("location_detail")
    val locationDetail: String,
    @SerializedName("price_per_min")
    val pricePerMin: Float,
    val startTimestamp: Long
)