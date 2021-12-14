package com.mvvm.myapplication.meeshotest.data.remotemodels

import com.google.gson.annotations.SerializedName

data class EndSessionRequest(
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("time_spent")
    val timeSpent: Int,
    @SerializedName("end_time")
    val endTime: Long
)