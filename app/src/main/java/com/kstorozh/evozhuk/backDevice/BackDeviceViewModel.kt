package com.kstorozh.evozhuk.backDevice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
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

    fun tryReturnDevice(): LiveData<Boolean> {
        return liveData<Boolean> {
            bookingSession.value?.let {
                Log.d("MainActivity", BookingInputData(bookingSession.value!!.userId, null).toString())
                emit(manageDeviceUseCases.returnDevice(BookingInputData(bookingSession.value!!.userId, null)))
            }
        }
    }

    fun getSessionData(): MutableLiveData<SessionData> {

        GlobalScope.launch {
            val data = manageDeviceUseCases.getSession()
            Log.d("MainActivity", "data is ${data?.let { data }}")
            data?.let {
                bookingSession.postValue(data)
            }
        }
        return bookingSession
    }

    fun setBookingSession(sessionData: SessionData) {
        bookingSession.value = sessionData
    }
}