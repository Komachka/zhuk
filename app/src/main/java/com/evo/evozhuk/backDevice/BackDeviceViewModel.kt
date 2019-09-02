package com.evo.evozhuk.backDevice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.evo.domainapi.GetBookingUseCase

import com.evo.domainapi.ManageDeviceUseCases
import com.evo.domainapi.model.BookingInputData
import com.evo.domainapi.model.NearbyDomainBooking
import com.evo.domainapi.model.SessionData
import com.evo.evozhuk.BaseViewModel
import com.evo.evozhuk.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class BackDeviceViewModel(
    private val manageDeviceUseCases: ManageDeviceUseCases,
    private val getBookingUseCase: GetBookingUseCase,
    private val applicationScope: CoroutineScope
) : BaseViewModel(manageDeviceUseCases, applicationScope) {

    private val bookingSession: MutableLiveData<SessionData> by lazy {
        MutableLiveData<SessionData>()
    }
    val nearbyBooking = MutableLiveData<NearbyDomainBooking>()
    fun tryReturnDevice(): LiveData<Boolean> {
        val returnDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            bookingSession.value?.let {
                val result = manageDeviceUseCases.returnDevice(
                    BookingInputData(bookingSession.value!!.userId, Calendar.getInstance(),
                        Calendar.getInstance(), false))
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

    fun getNearbyBooking(): LiveData<Event<Boolean>> {
        val isBookingExistsliveData = MutableLiveData<Event<Boolean>>()
        applicationScope.launch {
            val result = getBookingUseCase.getNearbyBooking()
            result.data?.let {
                nearbyBooking.postValue(it)
                isBookingExistsliveData.postValue(Event(true)) }
            result.domainError?.let {
                errors.postValue(Event(it))
                isBookingExistsliveData.postValue(Event(false))
            }
        }
        return isBookingExistsliveData
    }
}