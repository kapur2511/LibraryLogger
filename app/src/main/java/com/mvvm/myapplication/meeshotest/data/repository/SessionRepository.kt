package com.mvvm.myapplication.meeshotest.data.repository

import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData
import com.mvvm.myapplication.meeshotest.data.localmodels.EndSessionDisplayModel

interface SessionRepository {

    suspend fun endSession(barcodeData: BarcodeData): EndSessionDisplayModel?

    suspend fun addSession(barcodeData: BarcodeData)

    suspend fun getSession(): BarcodeData?
}