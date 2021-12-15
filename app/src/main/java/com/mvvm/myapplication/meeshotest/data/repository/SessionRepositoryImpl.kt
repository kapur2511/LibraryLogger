package com.mvvm.myapplication.meeshotest.data.repository

import com.mvvm.myapplication.meeshotest.data.datasource.SessionLocalDataSource
import com.mvvm.myapplication.meeshotest.data.datasource.SessionRemoteDataSource
import com.mvvm.myapplication.meeshotest.data.entities.SessionEntity
import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData
import com.mvvm.myapplication.meeshotest.data.remotemodels.EndSessionRequest
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource
): SessionRepository {

    override suspend fun endSession() {
        //using fail-fast approach here. End session should only be called
        //if a session is running.
        val session = sessionLocalDataSource.getSession()!!
        val endTime = System.currentTimeMillis()
        val timeSpent = TimeUnit.MILLISECONDS.toMinutes(
            endTime - session.sessionStartTimeStamp
        ).toInt()

        val endSessionRequest = EndSessionRequest(
            locationId = session.locationId,
            timeSpent = timeSpent,
            endTime = endTime
        )
        sessionRemoteDataSource.endSession(endSessionRequest = endSessionRequest)
        sessionLocalDataSource.endSession()
    }

    override suspend fun addSession(barcodeData: BarcodeData) {
        val sessionEntity = SessionEntity(
            sessionId = UUID.randomUUID().toString(),
            locationId = barcodeData.locationId,
            locationDetails = barcodeData.locationDetail,
            pricePerMin = barcodeData.pricePerMin,
            sessionStartTimeStamp = barcodeData.startTimestamp
        )

        sessionLocalDataSource.addSession(sessionEntity = sessionEntity)
    }

    override suspend fun getSession(): BarcodeData? {
        return sessionLocalDataSource.getSession()?.let { entity ->
            BarcodeData(
                locationId = entity.locationId,
                locationDetail = entity.locationDetails,
                pricePerMin = entity.pricePerMin,
                startTimestamp = entity.sessionStartTimeStamp
            )
        }
    }
}