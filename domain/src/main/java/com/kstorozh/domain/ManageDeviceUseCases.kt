package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.model.into.DeviceParam

class ManageDeviceUseCases(private val repository: DeviseRepository) {

    fun initDevice(deviceParam: DeviceParam): LiveData<Boolean> {
        return liveData {
            val isDeviceInit = repository.initDevice(deviceParam)
            emit(isDeviceInit)
        }
    }
}