package com.mvvm.myapplication.meeshotest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.myapplication.meeshotest.viewmodel.SessionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SessionViewModel::class)
    internal abstract fun bindSessionViewModel(viewModel: SessionViewModel): ViewModel
}