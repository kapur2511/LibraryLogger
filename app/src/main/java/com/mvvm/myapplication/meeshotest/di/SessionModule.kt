package com.mvvm.myapplication.meeshotest.di

import com.mvvm.myapplication.meeshotest.data.api.EndSessionApi
import com.mvvm.myapplication.meeshotest.data.dao.SessionDao
import com.mvvm.myapplication.meeshotest.data.database.AppDatabase
import com.mvvm.myapplication.meeshotest.data.datasource.SessionLocalDataSource
import com.mvvm.myapplication.meeshotest.data.datasource.SessionLocalDataSourceImpl
import com.mvvm.myapplication.meeshotest.data.datasource.SessionRemoteDataSource
import com.mvvm.myapplication.meeshotest.data.datasource.SessionRemoteDataSourceImpl
import com.mvvm.myapplication.meeshotest.data.repository.SessionRepository
import com.mvvm.myapplication.meeshotest.data.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@Module
class SessionModule {

    @Provides
    fun providesEndSessionApi(
        okHttpClient: OkHttpClient,
        factory: Converter.Factory
    ): EndSessionApi = Retrofit.Builder()
        .baseUrl("https://en478jh796m7w.x.pipedream.net")
        .addConverterFactory(factory)
        .client(okHttpClient)
        .build()
        .create(EndSessionApi::class.java)

    @Provides
    fun provideSessionDataSource(
        endSessionApi: EndSessionApi
    ): SessionRemoteDataSource = SessionRemoteDataSourceImpl(endSessionApi)

    @Provides
    fun providesSessionRepository(
        sessionRemoteDataSource: SessionRemoteDataSource,
        sessionLocalDataSource: SessionLocalDataSource
    ): SessionRepository = SessionRepositoryImpl(
        sessionRemoteDataSource,
        sessionLocalDataSource
    )

    @Provides
    fun providesSessionLocalDataSource(
        sessionDao: SessionDao
    ): SessionLocalDataSource = SessionLocalDataSourceImpl(
        sessionDao
    )

    @Provides
    fun providesSessionDao(
        appDatabase: AppDatabase
    ): SessionDao = appDatabase.sessionDao()

}