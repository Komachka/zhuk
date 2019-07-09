package com.kstorozh.data.database

import androidx.annotation.WorkerThread
import com.kstorozh.data.Device

class LocalDeviceData(private val deviceDao: DeviceDao) : ILocalDeviceData {

    @WorkerThread
    override suspend fun getDevice(): List<Device> {
        return deviceDao.getDeviceInfo()
    }

    @WorkerThread
    override suspend fun insertDevice(device: Device) {
        deviceDao.insertDevice(device)
    }

    @WorkerThread
    override suspend fun updateDevice(device: Device) {
        deviceDao.updateDevice(device)
    }

    @WorkerThread
    override suspend fun deleteAllDeviceInfo() {
        deviceDao.deleteAllDeviceInfo()
    }
}