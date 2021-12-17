package com.mvvm.myapplication.meeshotest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData
import com.mvvm.myapplication.meeshotest.data.localmodels.EndSessionDisplayModel
import com.mvvm.myapplication.meeshotest.data.repository.SessionRepository
import com.mvvm.myapplication.meeshotest.utils.Error
import com.mvvm.myapplication.meeshotest.utils.Loading
import com.mvvm.myapplication.meeshotest.utils.ResultWrapper
import com.mvvm.myapplication.meeshotest.utils.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
): ViewModel() {

    private val _sessionUIObservable = MutableLiveData<ResultWrapper<BarcodeData>>()
    val sessionUIObservable: LiveData<ResultWrapper<BarcodeData>> = _sessionUIObservable

    private val _endSessionAlertObservable = MutableLiveData<EndSessionDisplayModel>()
    val endSessionAlertObservable: LiveData<EndSessionDisplayModel> = _endSessionAlertObservable

    fun setupUiIfSessionIsRunning(){
        _sessionUIObservable.postValue(Loading(true))
        viewModelScope.launch(Dispatchers.IO) {
            val barcodeData = sessionRepository.getSession()
            if(barcodeData != null) {
                //there is already a session running, setup end session UI
                _sessionUIObservable.postValue(Success(barcodeData))
            } else {
                //no session is running, setup start session UI
                _sessionUIObservable.postValue(
                    Error(
                        Throwable("No active session, click on Start session to begin")
                    )
                )
            }
        }
    }

    fun checkAndStartSession(barcodeString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val barcodeData = parseString(barcodeString).copy(
                    startTimestamp =  System.currentTimeMillis()
                )
                sessionRepository.addSession(barcodeData)
                _sessionUIObservable.postValue(Success(barcodeData))
            } catch (e: Exception) {
                //Some parsing error
                _sessionUIObservable.postValue(Error(Throwable("Some parsing error, please try again.")))
            }
        }
    }

    fun endSession(barcodeString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val barcodeData = parseString(barcodeString)
                val endSessionDisplayModel = sessionRepository.endSession(barcodeData)
                if(endSessionDisplayModel != null) {
                    _endSessionAlertObservable.postValue(endSessionDisplayModel)
                } else {
                    _endSessionAlertObservable.postValue(
                        EndSessionDisplayModel(
                            amountToPay = 0f,
                            timeSpent = 0,
                            endTime = "",
                            errorString = "Please scan the same barcode to end session."
                        )
                    )
                }
            } catch (e: Exception) {
                _endSessionAlertObservable.postValue(
                    EndSessionDisplayModel(
                        amountToPay = 0f,
                        timeSpent = 0,
                        endTime = "",
                        errorString = "Something went wrong, please try again."
                    )
                )
            }

        }
    }

    private fun parseString(barcodeString: String): BarcodeData {
        val gson = Gson()
        val jsonElement = gson.fromJson(barcodeString, JsonElement::class.java).asString
        val type = object : TypeToken<BarcodeData>() {}.type
        return gson.fromJson(jsonElement, type)
    }
}