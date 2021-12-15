package com.mvvm.myapplication.meeshotest.data.datasource

import com.mvvm.myapplication.meeshotest.data.remotemodels.EndSessionRequest

interface SessionRemoteDataSource {

    suspend fun endSession(endSessionRequest: EndSessionRequest)
}