package com.mvvm.myapplication.meeshotest.data.datasource

import com.mvvm.myapplication.meeshotest.data.dao.SessionDao
import com.mvvm.myapplication.meeshotest.data.entities.SessionEntity
import javax.inject.Inject

class SessionLocalDataSourceImpl @Inject constructor(
    private val sessionDao: SessionDao
): SessionLocalDataSource {

    override suspend fun addSession(sessionEntity: SessionEntity) {
        sessionDao.insertSession(sessionEntity)
    }

    override suspend fun getSession(): SessionEntity? {
        return sessionDao.getSession()
    }

    override suspend fun endSession() {
        sessionDao.deleteAllSessions()
    }
}