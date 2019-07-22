package com.kstorozh.evozhuk.backDevice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class BackDeviceViewModel : ViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()


    fun tryReturnDevice(userId:String) : LiveData<Boolean>
    {
        return liveData<Boolean> {
            emit(manageDeviceUseCases.returnDevice(BookingInputData(userId, null)))
        }

    }
}