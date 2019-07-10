package com.kstorozh.dataimpl

import com.kstorozh.dataimpl.model.BookingParam
import com.kstorozh.dataimpl.model.DeviceParam

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam)
    suspend fun updateDevice(deviceParam: DeviceParam)
    suspend fun takeDevice(bookingParam: BookingParam)
    suspend fun returnDevice(deviceParam: DeviceParam)
}