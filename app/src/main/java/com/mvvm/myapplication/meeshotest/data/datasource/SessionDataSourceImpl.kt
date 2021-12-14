package com.mvvm.myapplication.meeshotest.data.datasource

import com.mvvm.myapplication.meeshotest.data.api.EndSessionApi
import javax.inject.Inject

class SessionDataSourceImpl @Inject constructor(
    val endSessionApi: EndSessionApi
): SessionDataSource {

    override suspend fun endSession() {

    }
}