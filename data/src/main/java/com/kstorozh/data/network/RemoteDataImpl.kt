package com.kstorozh.data.network

import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.StatusBody
import com.kstorozh.data.models.User

internal class RemoteDataImpl(
    private val deviceApi: DeviceApi,
    private val userApi: UserApi
) : RemoteData {

    override suspend fun initDevice(device: Device) {
        deviceApi.initDevice(device)
    }

    override suspend fun updateDevice(device: Device, deviceId: String) {
        deviceApi.updateDevice(device, deviceId)
    }

    override suspend fun takeDevise(bookingBody: BookingBody, deviceId: String) {
        deviceApi.takeDevice(bookingBody = bookingBody)
    }

    override suspend fun returnDevice(deviceId: String) {
        deviceApi.returnDevice(status = StatusBody(status = true), deviceId = deviceId)
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