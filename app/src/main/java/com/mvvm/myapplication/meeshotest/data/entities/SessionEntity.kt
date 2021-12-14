package com.mvvm.myapplication.meeshotest.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val SESSION_TABLE_NAME = "session_table"

@Entity(tableName = SESSION_TABLE_NAME)
data class SessionEntity(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val sessionId: String, //UUID
    @ColumnInfo(name = "location_id")
    val locationId: String,
    @ColumnInfo(name = "location_details")
    val locationDetails: String,
    @ColumnInfo(name = "price_per_min")
    val pricePerMin: Float,
    @ColumnInfo(name = "session_timestamp")
    val sessionStartTimeStamp: Long
)