package com.mvvm.myapplication.meeshotest.di

import com.mvvm.myapplication.meeshotest.LibraryApplication
import dagger.Component
import dagger.android.AndroidInjector

@Component(
    modules = [
        SessionBindingModule::class
    ]
)
interface ApplicationComponent: AndroidInjector<LibraryApplication> {

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
    }


}