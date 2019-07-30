package com.kstorozh.evozhuk.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.DomainErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {

    private val initDeviceUseCases: ManageDeviceUseCases by inject()
    private val errors:MutableLiveData<DomainErrors> = MutableLiveData<DomainErrors>()
    val applicationScope = CoroutineScope(Dispatchers.Default)

    fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean> {

        return liveData {
            val result = initDeviceUseCases.initDevice(deviceInputData)
            result.data?.let {
                emit(it)
            }
            result.domainError?.let {
                errors.postValue(it)
            }

        }
    }

    fun isDeviceInited(info: DeviceInputData): LiveData<Boolean> {
        return liveData {
            val result = initDeviceUseCases.isDeviceInited(info)
            result.data?.let {
                emit(it)
            }
            result.domainError?.let {
                errors.postValue(it)
            }
        }
    }
}