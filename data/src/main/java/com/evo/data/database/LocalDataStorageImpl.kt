package com.evo.data.database

import com.evo.data.models.BookingBody
import com.evo.data.models.Device

internal class LocalDataStorageImpl(
    private val deviceDao: DeviceDao,
    private val bookingDao: BookingDao
) : LocalDataStorage {
    override suspend fun deleteBookingInfo() {
        bookingDao.deleteAllBooking()
    }

    override suspend fun getBookingByDeviceId(deviceId: String): BookingBody? {
        return bookingDao.getBookingInfoByDeviceId(deviceId.toInt())
    }

    override suspend fun saveBooking(bookingBody: BookingBody) {
        bookingDao.insertBooking(bookingBody)
    }

    override suspend fun getDeviceInfo(): Device? {
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