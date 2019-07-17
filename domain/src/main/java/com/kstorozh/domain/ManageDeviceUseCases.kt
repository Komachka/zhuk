package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam

class ManageDeviceUseCases(private val repository: DeviseRepository) {

    fun initDevice(deviceParam: DeviceParam): LiveData<Boolean> {
        return liveData {
            val isDeviceInit = repository.initDevice(deviceParam)
            emit(isDeviceInit)
        }
    }
    
    fun takeDevice(bookingParam: BookingParam) : LiveData<Boolean>
    {
        return liveData {
            val isDeviceTaken = repository.takeDevice(bookingParam)
            emit(isDeviceTaken)
        }
    }


    fun returnDevice(deviceParam: DeviceParam) : LiveData<Boolean>
    {
        return liveData {
            val isDeviceReturned =  repository.returnDevice(deviceParam)
            emit(isDeviceReturned)
        }
    }
}