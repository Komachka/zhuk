package com.kstorozh.data.database

import androidx.annotation.WorkerThread
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import org.koin.core.KoinComponent

internal interface LocalDataStorage : KoinComponent {

    @WorkerThread
    suspend fun getDeviceInfo(): Device

    @WorkerThread
    suspend fun insertDevice(device: Device)

    @WorkerThread
    suspend fun updateDevice(device: Device)

    @WorkerThread
    suspend fun deleteAllDeviceInfo()

    @WorkerThread
    suspend fun saveBooking(bookingBody: BookingBody)

    @WorkerThread
    suspend fun getBookingByDeviceId(deviceId: String): BookingBody

    @WorkerThread
    suspend fun deleteBookingInfo()
}