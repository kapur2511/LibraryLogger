package com.mvvm.myapplication.meeshotest.utils

sealed class ResultWrapper<T>

data class Loading<T>(
    val showLoading: Boolean
): ResultWrapper<T>()

data class Success<T>(
    val data: T
): ResultWrapper<T>()

data class Error<T>(
    val throwable: Throwable?
): ResultWrapper<T>()