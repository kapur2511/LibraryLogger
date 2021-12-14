package com.mvvm.myapplication.meeshotest.data.repository

import com.mvvm.myapplication.meeshotest.data.datasource.SessionDataSource
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    val sessionDataSource: SessionDataSource
): SessionRepository {

    override suspend fun endSession() {

    }
}