package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domainimpl.ManageDeviceUseCases
import com.kstorozh.domainimpl.model.BookingInputData
import java.util.*

class ManageDeviceUseCasesImpl(private val repository: DeviseRepository, val mapper: DeviceInfoMapper) :
    ManageDeviceUseCases {

    override fun initDevice(deviceInputData: com.kstorozh.domainimpl.model.DeviceInputData): LiveData<Boolean> {
        return liveData {
            val isDeviceInit = repository.initDevice(mapper.mapDeviceInfoToDeviceParam(deviceInputData))
            emitSource(isDeviceInit)
        }
    }

    override fun takeDevice(bookingParam: BookingInputData): LiveData<Boolean> {
        val startDate = Date()
        return liveData {
            val isDeviceTaken = repository.takeDevice(mapper.mapBookingParam(bookingParam, startDate))
            emitSource(isDeviceTaken)
        }
    }

    override fun returnDevice(bookingParam: BookingInputData): LiveData<Boolean> {
        return liveData {
            val isDeviceReturned = repository.returnDevice(mapper.mapBookingParam(bookingParam))
            emitSource(isDeviceReturned)
        }
    }
}