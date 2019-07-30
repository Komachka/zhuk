package com.kstorozh.evozhuk.backDevice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.SessionData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class BackDeviceViewModel : ViewModel(), KoinComponent {

    private val bookingSession: MutableLiveData<SessionData> by lazy {
        MutableLiveData<SessionData>()
    }

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val errors:MutableLiveData<DomainErrors> = MutableLiveData<DomainErrors>()

    fun tryReturnDevice(): LiveData<Boolean> {
        return liveData<Boolean> {
            bookingSession.value?.let {
                Log.d("MainActivity", BookingInputData(bookingSession.value!!.userId, null).toString())
                val result = manageDeviceUseCases.returnDevice(BookingInputData(bookingSession.value!!.userId, null))
                result.data?.let {
                    emit(it)
                }
                result.domainError?.let {
                    errors.postValue(it)
                }
            }
        }
    }

    fun getSessionData(): MutableLiveData<SessionData> {

        GlobalScope.launch {
            val domainResult = manageDeviceUseCases.getSession()
            Log.d("MainActivity", "data is ${domainResult?.let { domainResult }}")
            domainResult.data?.let {
                bookingSession.postValue(it)
            }
            domainResult.domainError?.let {
                errors.postValue(it)
            }
        }
        return bookingSession
    }

    fun setBookingSession(sessionData: SessionData) {
        bookingSession.value = sessionData
    }
}