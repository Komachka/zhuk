package com.kstorozh.dataimpl

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam)
    suspend fun updateDevice(deviceParam: DeviceParam)
    suspend fun takeDevice(bookingParam: BookingParam)
    suspend fun returnDevice(deviceParam: DeviceParam)
}