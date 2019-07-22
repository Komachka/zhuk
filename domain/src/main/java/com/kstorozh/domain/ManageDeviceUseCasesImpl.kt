package com.kstorozh.domain

import android.util.Log
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import org.koin.core.KoinComponent

import java.util.*

class ManageDeviceUseCasesImpl(private val repository: DeviseRepository, val mapper: DeviceInfoMapper) :
    ManageDeviceUseCases, KoinComponent {

    override suspend fun initDevice(deviceInputData: DeviceInputData): Boolean {

        val deviceParam = mapper.mapDeviceInfoToDeviceParam(deviceInputData)
        if (!repository.deviceAlreadyInited(deviceParam))
            return repository.initDevice(deviceParam)
        else return true
    }

    override suspend fun takeDevice(bookingParam: BookingInputData): Boolean {

        val startDate = Calendar.getInstance()
        val res = repository.takeDevice(mapper.mapBookingParam(bookingParam, startDate))
        Log.d("MainActivity", "is device taken ${res.toString()}")
        return res
    }

    override suspend fun returnDevice(bookingParam: BookingInputData): Boolean {

            return repository.returnDevice(mapper.mapBookingParam(bookingParam))
    }
}