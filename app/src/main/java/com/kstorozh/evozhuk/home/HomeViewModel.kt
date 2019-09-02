package com.kstorozh.evozhuk.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val initDeviceUseCases: ManageDeviceUseCases,
    private val applicationScope: CoroutineScope
) : BaseViewModel(initDeviceUseCases, applicationScope) {

    fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean> {
        val initDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            val result = initDeviceUseCases.initDevice(deviceInputData)
            result.data?.let {
                initDeviceLiveData.postValue(it)
            }
            result.domainError?.let {
                errors.postValue(Event(it))
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
                errors.postValue(Event(it))
            }
        }
        return isDeviceInitedLiveData
    }
}