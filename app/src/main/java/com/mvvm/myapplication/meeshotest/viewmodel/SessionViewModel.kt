package com.mvvm.myapplication.meeshotest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData
import com.mvvm.myapplication.meeshotest.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
): ViewModel() {

    private val _sessionUIObservable = MutableLiveData<BarcodeData>()
    val sessionUIObservable = _sessionUIObservable

    fun setupUiIfSessionIsRunning(){
        viewModelScope.launch(Dispatchers.IO) {
            val barcodeData = sessionRepository.getSession()
            if(barcodeData != null) {
                //there is already a session running, setup end session UI
                _sessionUIObservable.postValue(barcodeData)
            } else {
                //no session is running, setup start session UI
            }
        }
    }

    fun checkAndStartSession(barcodeString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gson = Gson()
                val type = object: TypeToken<BarcodeData>(){}.type
                val barcodeData = gson.fromJson<BarcodeData>(barcodeString, type)
                sessionRepository.addSession(
                    barcodeData = barcodeData.copy(
                        startTimestamp =  System.currentTimeMillis()
                    )
                )
                _sessionUIObservable.postValue(barcodeData)
            } catch (e: Exception) {
                //Some parsing error
            }
        }
    }

    fun endSession() {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.endSession()
        }
    }
}