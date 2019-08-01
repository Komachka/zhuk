package com.kstorozh.evozhuk.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.evozhuk.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : BaseViewModel(), KoinComponent {

    private val initDeviceUseCases: ManageDeviceUseCases by inject()
    val applicationScope = CoroutineScope(Dispatchers.Default)

    fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean> {
        val initDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            val result = initDeviceUseCases.initDevice(deviceInputData)
            result.data?.let {
                initDeviceLiveData.postValue(it)
            }
            result.domainError?.let {
                errors.postValue(it)
            }
        }
        return initDeviceLiveData
    }

    fun isDeviceInited(info: DeviceInputData): LiveData<Boolean> {
        val isDeviceInitedLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            val result = initDeviceUseCases.isDeviceInited(info)
            result.data?.let {
                isDeviceInitedLiveData.postValue(it)
            }
            result.domainError?.let {
                errors.postValue(it)
            }
        }
        return isDeviceInitedLiveData
    }
}