package com.evo.data.database

import androidx.annotation.WorkerThread
import com.evo.data.models.BookingBody
import com.evo.data.models.Device

internal interface LocalDataStorage {

    @WorkerThread
    suspend fun getDeviceInfo(): Device?

    @WorkerThread
    suspend fun insertDevice(device: Device)

    @WorkerThread
    suspend fun updateDevice(device: Device)

    @WorkerThread
    suspend fun deleteAllDeviceInfo()

    @WorkerThread
    suspend fun saveBooking(bookingBody: BookingBody)

    @WorkerThread
    suspend fun getBookingByDeviceId(deviceId: String): BookingBody?

    @WorkerThread
    suspend fun deleteBookingInfo()
}