package com.kstorozh.data.repository

import com.kstorozh.data.BookingBody
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.network.RemoteData

class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData
) : DeviseRepository
{
    override suspend fun takeDevice(bookingBody: BookingBody) {
        val device = localData.getDeviceInfo()
        remoteData.takeDevise(bookingBody, device)
    }

    override suspend fun returnDevice() {
        val device = localData.getDeviceInfo()
        remoteData.returnDevice(device)
    }

    override suspend fun updateDevice() {
        val device = localData.getDeviceInfo()
        remoteData.updateDevice(device)
    }

    override suspend fun initDevice() {
        val device = localData.getDeviceInfo()
        remoteData.initDevice(device)
    }






}