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

    fun isDeviceInited(deviceInputData: DeviceInputData): LiveData<Boolean> {

        return liveData {
            val result = initDeviceUseCases.initDevice(deviceInputData)
            emit(result)
        }
    }


}