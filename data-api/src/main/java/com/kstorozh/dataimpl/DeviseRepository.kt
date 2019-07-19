package com.kstorozh.dataimpl

import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam): Boolean
    suspend fun updateDevice(deviceParam: DeviceParam): Boolean
    suspend fun takeDevice(bookingParam: BookingParam): Boolean
    suspend fun returnDevice(bookingParam: BookingParam): Boolean
    suspend fun getErrors(): MyError?
}