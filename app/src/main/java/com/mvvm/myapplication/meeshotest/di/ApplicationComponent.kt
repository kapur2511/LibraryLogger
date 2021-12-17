package com.mvvm.myapplication.meeshotest.di

import com.mvvm.myapplication.meeshotest.LibraryApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SessionBindingModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface ApplicationComponent: AndroidInjector<LibraryApplication> {

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        @BindsInstance
        fun application(application: LibraryApplication): Builder
    }

}