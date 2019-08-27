package com.kstorozh.evozhuk.backDevice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kstorozh.domainapi.GetBookingUseCase

import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.NearbyDomainBooking
import com.kstorozh.domainapi.model.SessionData
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class BackDeviceViewModel : BaseViewModel(), KoinComponent {

    private val bookingSession: MutableLiveData<SessionData> by lazy {
        MutableLiveData<SessionData>()
    }

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val getBookingUseCase:GetBookingUseCase by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    val nearbyBooking = MutableLiveData<NearbyDomainBooking>()


    fun tryReturnDevice(): LiveData<Boolean> {
        val returnDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            bookingSession.value?.let {
                val result = manageDeviceUseCases.returnDevice(BookingInputData(bookingSession.value!!.userId, Calendar.getInstance(), Calendar.getInstance()))
                result.data?.let {
                    returnDeviceLiveData.postValue(it)
                }
                result.domainError?.let {
                    errors.postValue(Event(it))
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
                errors.postValue(Event(it))
            }
        }
        return bookingSession
    }

    fun setBookingSession(sessionData: SessionData) {
        bookingSession.value = sessionData
    }

    fun getNearbyBooking(): LiveData<Boolean> {
        val isBookingExistsliveData = MutableLiveData<Boolean>()
        applicationScope.launch {
            val result = getBookingUseCase.getNearbyBooking()
            result.data?.let {
                nearbyBooking.postValue(it)
                isBookingExistsliveData.postValue(true) }
            result.domainError?.let {
                errors.postValue(Event(it))
                isBookingExistsliveData.postValue(false)
            }
        }
        return isBookingExistsliveData

    }
}