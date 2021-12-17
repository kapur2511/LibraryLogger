package com.mvvm.myapplication.meeshotest.data.localmodels

data class EndSessionDisplayModel(
    val timeSpent: Int,
    val endTime: String,
    val amountToPay: Float,
    val errorString: String? = null
)