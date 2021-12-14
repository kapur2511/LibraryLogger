package com.mvvm.myapplication.meeshotest.data.repository

interface SessionRepository {

    suspend fun endSession()
}