package com.kstorozh.evozhuk.chooseTime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ChooseTimeSharedViewModel : ViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()


    val choosenData:MutableLiveData<Calendar> by lazy { MutableLiveData<Calendar>() }

    fun setData(calendar: Calendar)
    {
        choosenData.value = calendar
    }

    fun getData(): MutableLiveData<Calendar> {
        return choosenData
    }


    fun tryBookDevice(userId:String) : LiveData<Boolean>
    {
        return liveData<Boolean> {
            emit(manageDeviceUseCases.takeDevice(BookingInputData(userId, choosenData.value!!)))
        }

    }
}