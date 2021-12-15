package com.mvvm.myapplication.meeshotest.data.datasource

import com.mvvm.myapplication.meeshotest.data.entities.SessionEntity

interface SessionLocalDataSource {

    suspend fun addSession(sessionEntity: SessionEntity)

    suspend fun getSession(): SessionEntity?

    suspend fun endSession()
}