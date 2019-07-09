package com.kstorozh.data.database

import androidx.annotation.WorkerThread
import com.kstorozh.data.Device

internal interface LocalDataStorage {

    @WorkerThread
    suspend fun getDeviceInfo(): Device

    @WorkerThread
    suspend fun insertDevice(device: Device)

    @WorkerThread
    suspend fun updateDevice(device: Device)

    @WorkerThread
    suspend fun deleteAllDeviceInfo()
}