package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domainimpl.model.BookingInputData
import com.kstorozh.domainimpl.model.DeviceInputData
import java.util.*

class ManageDeviceUseCases(private val repository: DeviseRepository, val mapper: DeviceInfoMapper) {

    fun initDevice(deviceInputData: com.kstorozh.domainimpl.model.DeviceInputData): LiveData<Boolean> {
        return liveData {
            val isDeviceInit = repository.initDevice(mapper.mapDeviceInfoToDeviceParam(deviceInputData))
            emit(isDeviceInit)
        }
    }

    fun takeDevice(bookingParam: com.kstorozh.domainimpl.model.BookingInputData): LiveData<Boolean> {
        val startDate = Date()
        return liveData {
            val isDeviceTaken = repository.takeDevice(mapper.mapBookingParam(bookingParam, startDate))
            emit(isDeviceTaken)
        }
    }

    fun returnDevice(bookingParam: com.kstorozh.domainimpl.model.BookingInputData): LiveData<Boolean> {
        return liveData {
            val isDeviceReturned = repository.returnDevice(mapper.mapBookingParam(bookingParam))
            emit(isDeviceReturned)
        }
    }
}