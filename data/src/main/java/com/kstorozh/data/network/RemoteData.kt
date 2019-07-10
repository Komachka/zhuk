package com.kstorozh.data.network

import androidx.annotation.WorkerThread
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.User

internal interface RemoteData {

    @WorkerThread
    suspend fun initDevice(device: Device)

    @WorkerThread
    suspend fun updateDevice(device: Device, deviceId: String)

    @WorkerThread
    suspend fun takeDevise(bookingBody: BookingBody, deviceId: String)

    @WorkerThread
    suspend fun returnDevice(deviceId: String)

    @WorkerThread
    suspend fun getUsers(): List<User>

    @WorkerThread
    suspend fun createUser(user: User)

    @WorkerThread
    suspend fun remindPin(user: User)
}