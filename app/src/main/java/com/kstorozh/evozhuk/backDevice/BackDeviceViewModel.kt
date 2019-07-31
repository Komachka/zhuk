package com.kstorozh.evozhuk.backDevice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.SessionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class BackDeviceViewModel : ViewModel(), KoinComponent {

    private val bookingSession: MutableLiveData<SessionData> by lazy {
        MutableLiveData<SessionData>()
    }

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    val errors: MutableLiveData<DomainErrors> = MutableLiveData<DomainErrors>()
    val returnDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun tryReturnDevice(): LiveData<Boolean> {

        applicationScope.launch {
            bookingSession.value?.let {
                val result = manageDeviceUseCases.returnDevice(BookingInputData(bookingSession.value!!.userId, null))
                result.data?.let {
                    returnDeviceLiveData.postValue(it)
                }
                result.domainError?.let {
                    errors.postValue(it)
                }
            }
        }
        return returnDeviceLiveData
    }

    fun getSessionData(): MutableLiveData<SessionData> {

        applicationScope.launch {
            val domainResult = manageDeviceUseCases.getSession()
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