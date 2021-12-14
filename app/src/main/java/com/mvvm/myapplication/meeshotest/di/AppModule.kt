package com.mvvm.myapplication.meeshotest.di

import android.content.Context
import androidx.room.Room
import com.mvvm.myapplication.meeshotest.data.database.AppDatabase
import dagger.Module

@Module
class AppModule {

    fun providesAppDatabase(
        context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,"app_database"
        ).build()
}