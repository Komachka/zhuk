package com.kstorozh.evozhuk.chooseTime

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class SpecificTimeAndDateViewModel : BaseViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val getBookingsUseCase: GetBookingUseCase by inject()

    fun editCurrentBooking(endDate: Long): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        applicationScope.launch {
            val result = manageDeviceUseCases.editCurrentBooking(endDate)
            result.data?.let {
                liveData.postValue(it)
            }
            result.domainError?.let {
                errors.postValue(Event(it))
                liveData.postValue(false)
            }
        }
        return liveData
    }
}