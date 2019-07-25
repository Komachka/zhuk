package com.kstorozh.evozhuk.chooseTime

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.evozhuk.notifications.LOG_TAG
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*

class ChooseTimeSharedViewModel : ViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()

    val choosenCalendar: MutableLiveData<Calendar> by lazy { MutableLiveData<Calendar>().also {
        it.value = GregorianCalendar.getInstance()
    } }
    val userId: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun setCalendar(milisec: Long) {
        choosenCalendar.value?.timeInMillis = milisec
    }

    fun setUserId(id: String) {
        userId.value = id
    }



    fun tryBookDevice(): LiveData<Boolean> {
        Log.d("MainActivity", SimpleDateFormat("HH:mm dd MMMM").format(choosenCalendar.value?.timeInMillis) + " user id ${userId.value}")
        return liveData<Boolean> {
            emit(manageDeviceUseCases.takeDevice(BookingInputData(userId.value!!, choosenCalendar.value)))
        }
    }
}