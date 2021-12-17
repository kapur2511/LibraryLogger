package com.mvvm.myapplication.meeshotest.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mvvm.myapplication.meeshotest.LibraryApplication
import com.mvvm.myapplication.meeshotest.data.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun appContext(application: LibraryApplication): Context = application

    @Provides
    fun providesAppDatabase(
        context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,"app_database"
        ).build()
}