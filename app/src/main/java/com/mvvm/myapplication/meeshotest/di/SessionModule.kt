package com.mvvm.myapplication.meeshotest.di

import com.mvvm.myapplication.meeshotest.data.api.EndSessionApi
import com.mvvm.myapplication.meeshotest.data.datasource.SessionDataSource
import com.mvvm.myapplication.meeshotest.data.datasource.SessionDataSourceImpl
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
    ): SessionDataSource = SessionDataSourceImpl(endSessionApi)

    @Provides
    fun providesSessionRepository(
        sessionDataSource: SessionDataSource
    ): SessionRepository = SessionRepositoryImpl(sessionDataSource)
}