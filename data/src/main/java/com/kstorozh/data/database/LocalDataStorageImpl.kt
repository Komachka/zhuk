package com.kstorozh.data.database

import com.kstorozh.data.Device

class LocalDataStorageImpl(private val deviceDao: DeviceDao) : LocalDataStorage {


    override suspend fun getDeviceInfo(): Device {
        return deviceDao.getDeviceInfo()
    }

    override suspend fun insertDevice(device: Device) {
        deviceDao.insertDevice(device)
    }

    override suspend fun updateDevice(device: Device) {
        deviceDao.updateDevice(device)
    }

    override suspend fun deleteAllDeviceInfo() {
        deviceDao.deleteAllDeviceInfo()
    }
}