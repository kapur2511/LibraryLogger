package com.mvvm.myapplication.meeshotest.data.datasource

import com.mvvm.myapplication.meeshotest.data.api.EndSessionApi
import com.mvvm.myapplication.meeshotest.data.remotemodels.EndSessionRequest
import javax.inject.Inject

class SessionRemoteDataSourceImpl @Inject constructor(
    private val endSessionApi: EndSessionApi
): SessionRemoteDataSource {

    override suspend fun endSession(endSessionRequest: EndSessionRequest) {
        endSessionApi.endSession(endSessionRequest)
    }
}