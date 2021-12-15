package com.mvvm.myapplication.meeshotest.data.dao

import androidx.room.*
import com.mvvm.myapplication.meeshotest.data.entities.SESSION_TABLE_NAME
import com.mvvm.myapplication.meeshotest.data.entities.SessionEntity

@Dao
interface SessionDao {

    @Query("SELECT * FROM $SESSION_TABLE_NAME LIMIT 1")
    suspend fun getSession(): SessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(sessionEntity: SessionEntity)

    @Delete
    suspend fun deleteSession(sessionEntity: SessionEntity)

    @Query("DELETE FROM $SESSION_TABLE_NAME")
    suspend fun deleteAllSessions()
}