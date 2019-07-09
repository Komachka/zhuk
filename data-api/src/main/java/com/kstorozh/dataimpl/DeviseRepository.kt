package com.kstorozh.dataimpl

interface DeviseRepository {
    suspend fun initDevice()
    suspend fun updateDevice()
    suspend fun takeDevice(deviceParam: DeviceParam)
    suspend fun returnDevice()


}