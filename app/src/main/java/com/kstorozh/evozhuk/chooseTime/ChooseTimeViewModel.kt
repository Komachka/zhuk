package com.kstorozh.evozhuk.chooseTime

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.evozhuk.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ChooseTimeViewModel : BaseViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    val chooseCalendar: MutableLiveData<Calendar> by lazy { MutableLiveData<Calendar>().also {
        it.value = GregorianCalendar.getInstance()
    } }
    val userIdLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun setCalendar(millisec: Long) {
        chooseCalendar.value?.timeInMillis = millisec
    }

    fun setUserId(id: String) {
        userIdLiveData.value = id
    }

    @SuppressLint("SimpleDateFormat")
    fun tryBookDevice(timeMs: Long): LiveData<Boolean> {
        val bookDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        userIdLiveData.value?.let {
            applicationScope.launch {
                val result = manageDeviceUseCases.takeDevice(BookingInputData(it, Calendar.getInstance().apply { timeInMillis = timeMs }))
                result.data?.let {
                    bookDeviceLiveData.postValue(it)
                }
                result.domainError?.let {
                    errors.postValue(it)
                }
            }
        }
        return bookDeviceLiveData
    }
}