package com.kstorozh.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import org.koin.core.KoinComponent

import java.util.*

class ManageDeviceUseCasesImpl(private val repository: DeviseRepository, val mapper: DeviceInfoMapper) :
    ManageDeviceUseCases, KoinComponent {

    override fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean> {
        return liveData {
            Log.d("MainActivity", "HERE")
            val isDeviceInit = repository.initDevice(mapper.mapDeviceInfoToDeviceParam(deviceInputData))
            Log.d("MainActivity", "is device init ${isDeviceInit.value}")
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