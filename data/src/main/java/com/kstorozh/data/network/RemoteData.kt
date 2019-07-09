package com.kstorozh.data.network

import androidx.annotation.WorkerThread
import com.kstorozh.data.BookingBody
import com.kstorozh.data.Device
import com.kstorozh.data.User

interface RemoteData {

    @WorkerThread
    suspend fun initDevice(device: Device)

    @WorkerThread
    suspend fun updateDevice(device: Device)

    @WorkerThread
    suspend fun takeDevise(bookingBody: BookingBody, device: Device)

    @WorkerThread
    suspend fun returnDevice(device: Device)

    @WorkerThread
    suspend fun getUsers(): List<User>

    @WorkerThread
    suspend fun createUser(user: User)

    @WorkerThread
    suspend fun remindPin(user: User)
}