package com.kstorozh.evozhuk.chooseTime

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.ErrorStatus
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.Event
import com.kstorozh.evozhuk.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ChooseTimeViewModel : BaseViewModel(), KoinComponent {

    private val manageDeviceUseCases: ManageDeviceUseCases by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    val conflictBookingLiveData = MutableLiveData<Boolean>()

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
    fun tryBookDevice(timeMs: Long, isForced: Boolean = false): LiveData<Boolean> {
        val bookDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        userIdLiveData.value?.let {
            applicationScope.launch {
                val result = manageDeviceUseCases.takeDevice(
                    BookingInputData(it, Calendar.getInstance(), Calendar.getInstance().apply { timeInMillis = timeMs }, isForce = isForced))
                result.data?.let {
                    bookDeviceLiveData.postValue(it)
                }
                result.domainError?.let {
                    if (it.errorStatus != null) {
                        Log.d(LOG_TAG, "${it.errorStatus}")
                        if (it.errorStatus == ErrorStatus.CONFLICT_ERROR) {
                            conflictBookingLiveData.postValue(true)
                        }
                    }
                    errors.postValue(Event(it))
                }
            }
        }
        return bookDeviceLiveData
    }
}