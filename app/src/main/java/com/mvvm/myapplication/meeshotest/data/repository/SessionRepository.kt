package com.mvvm.myapplication.meeshotest.data.repository

import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData

interface SessionRepository {

    suspend fun endSession()

    suspend fun addSession(barcodeData: BarcodeData)

    suspend fun getSession(): BarcodeData?
}