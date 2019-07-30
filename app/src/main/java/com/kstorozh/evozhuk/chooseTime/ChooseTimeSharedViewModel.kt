package com.kstorozh.evozhuk.chooseTime

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.evozhuk.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*

class ChooseTimeSharedViewModel : ViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val errors:MutableLiveData<DomainErrors> = MutableLiveData<DomainErrors>()
    val applicationScope = CoroutineScope(Dispatchers.Default)

    val chooseCalendar: MutableLiveData<Calendar> by lazy { MutableLiveData<Calendar>().also {
        it.value = GregorianCalendar.getInstance()
    } }
    val userId: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun setCalendar(millisec: Long) {
        chooseCalendar.value?.timeInMillis = millisec
    }

    fun setUserId(id: String) {
        userId.value = id
    }

    @SuppressLint("SimpleDateFormat")
    fun tryBookDevice(): LiveData<Boolean> {
        Log.d(LOG_TAG, SimpleDateFormat(TimeUtils.dateFormat).format(chooseCalendar.value?.timeInMillis) + " user id ${userId.value}")
        return liveData<Boolean> {
            val result = manageDeviceUseCases.takeDevice(BookingInputData(userId.value!!, chooseCalendar.value))
            result.data?.let {
                emit(it)
            }
            result.domainError?.let {
                errors.postValue(it)
            }
        }
    }
}