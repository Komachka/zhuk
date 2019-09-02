package com.kstorozh.evozhuk.chooseTime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class SpecificTimeAndDateViewModel(
    private val manageDeviceUseCases: ManageDeviceUseCases,
    private val applicationScope: CoroutineScope
) : BaseViewModel() {

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