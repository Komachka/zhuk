package com.kstorozh.domain

import android.util.Log
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.SessionData
import org.koin.core.KoinComponent

import java.util.*

class ManageDeviceUseCasesImpl(private val repository: DeviseRepository, val mapper: DeviceInfoMapper) :
    ManageDeviceUseCases, KoinComponent {
    override suspend fun isDeviceInited(deviceInputData: DeviceInputData): Boolean {
        val deviceParam = mapper.mapDeviceInfoToDeviceParam(deviceInputData)
        return repository.deviceAlreadyInited(deviceParam)
    }

    override suspend fun getSession(): SessionData? {
        val result = repository.getBookingSession()
        if (result != null) return mapper.mapBookingSession(result) else return null
    }

    override suspend fun initDevice(deviceInputData: DeviceInputData): Boolean {

        val deviceParam = mapper.mapDeviceInfoToDeviceParam(deviceInputData)
        return repository.initDevice(deviceParam)

    }

    override suspend fun takeDevice(bookingParam: BookingInputData): Boolean {

        val startDate = Calendar.getInstance()
        val res = repository.takeDevice(mapper.mapBookingParam(bookingParam, startDate))
        return res
    }

    override suspend fun returnDevice(bookingParam: BookingInputData): Boolean {
            val booking = mapper.mapBookingParam(bookingParam)
            val res = repository.returnDevice(booking)
            return res
    }
}