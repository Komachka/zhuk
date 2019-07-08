package com.kstorozh.data.database

import com.kstorozh.data.Device

interface ILocalDeviceData {

    suspend fun getDevice(): List<Device> ;
    suspend fun insertDevice(device: Device)
    suspend fun updateDevice(device: Device)
    suspend fun deleteAllDeviceInfo()
}