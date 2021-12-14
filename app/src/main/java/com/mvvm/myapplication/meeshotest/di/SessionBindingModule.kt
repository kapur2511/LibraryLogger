package com.mvvm.myapplication.meeshotest.di

import com.mvvm.myapplication.meeshotest.ui.SessionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [SessionModule::class, NetworkModule::class])
abstract class SessionBindingModule {

    @ContributesAndroidInjector
    abstract fun bindSessionActivity(): SessionActivity
}