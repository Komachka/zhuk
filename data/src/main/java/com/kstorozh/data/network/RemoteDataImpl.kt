package com.kstorozh.data.network

import com.kstorozh.data.BookingBody
import com.kstorozh.data.Device
import com.kstorozh.data.StatusBody
import com.kstorozh.data.User

internal class RemoteDataImpl(
    private val deviceApi: DeviceApi,
    private val userApi: UserApi
) : RemoteData {

    override suspend fun initDevice(device: Device) {
        deviceApi.initDevice(device)
    }

    override suspend fun updateDevice(device: Device) {
        deviceApi.updateDevice(device = device, deviceId = device.uid)
    }

    override suspend fun takeDevise(bookingBody: BookingBody, device: Device) {
        deviceApi.takeDevice(bookingBody = bookingBody)
    }

    override suspend fun returnDevice(device: Device) {
        deviceApi.returnDevice(status = StatusBody(status = true), deviceId = device.uid)
    }

    override suspend fun getUsers(): List<User> {
        return userApi.getUsers()
    }

    override suspend fun createUser(user: User) {
        userApi.createUser(user = user)
    }

    override suspend fun remindPin(user: User) {
        userApi.remindPin(userId = user.id.toString())
    }
}