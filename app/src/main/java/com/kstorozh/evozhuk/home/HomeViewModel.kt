package com.kstorozh.evozhuk.home

import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInputData
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {

    private val initDeviceUseCases: ManageDeviceUseCases by inject()

    fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean> {

        return liveData {
            val result = initDeviceUseCases.initDevice(deviceInputData)
            emit(result)
        }
    }

    fun isDeviceInited(info: DeviceInputData): LiveData<Boolean> {
        return liveData {
            val result = initDeviceUseCases.isDeviceInited(info)
            emit(result)
        }
    }
}