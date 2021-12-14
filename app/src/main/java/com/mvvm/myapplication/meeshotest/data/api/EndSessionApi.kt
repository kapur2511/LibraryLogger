package com.mvvm.myapplication.meeshotest.data.api

import com.mvvm.myapplication.meeshotest.data.remotemodels.EndSessionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

private const val END_SESSION_API = "/submit-session"

interface EndSessionApi {

    @POST(END_SESSION_API)
    suspend fun endSession(
        @Body endSessionRequest: EndSessionRequest
    ): Response<Any>
}