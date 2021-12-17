package com.mvvm.myapplication.meeshotest.data.repository

import android.text.format.DateFormat
import com.mvvm.myapplication.meeshotest.data.datasource.SessionLocalDataSource
import com.mvvm.myapplication.meeshotest.data.datasource.SessionRemoteDataSource
import com.mvvm.myapplication.meeshotest.data.entities.SessionEntity
import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData
import com.mvvm.myapplication.meeshotest.data.localmodels.EndSessionDisplayModel
import com.mvvm.myapplication.meeshotest.data.remotemodels.EndSessionRequest
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource
): SessionRepository {

    override suspend fun endSession(barcodeData: BarcodeData): EndSessionDisplayModel? {
        //using fail-fast approach here. End session should only be called
        //if a session is running.
        val session = sessionLocalDataSource.getSession()!!

        if(barcodeData.locationId != session.locationId) {
            //This is not the same barcode
            return null
        }

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

        return EndSessionDisplayModel(
            timeSpent = timeSpent,
            endTime = getDate(endTime),
            amountToPay = timeSpent * session.pricePerMin
        )
    }

    private fun getDate(timestamp: Long) :String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy hh:mm:ss",calendar).toString()
        return date
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