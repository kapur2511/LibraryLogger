package com.mvvm.myapplication.meeshotest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvvm.myapplication.meeshotest.data.dao.SessionDao
import com.mvvm.myapplication.meeshotest.data.entities.SessionEntity

@Database(
    entities = [
        SessionEntity::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun sessionDao(): SessionDao
}