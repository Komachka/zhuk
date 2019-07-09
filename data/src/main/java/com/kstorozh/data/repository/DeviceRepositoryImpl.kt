package com.kstorozh.data.repository

import com.kstorozh.data.BookingBody
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.network.RemoteData
import com.kstorozh.dataimpl.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData
) : DeviseRepository
{
    override suspend fun takeDevice(deviceParam: DeviceParam) {

        //TODO
        //val device = localData.getDeviceInfo()
        //замапить
        //remoteData.takeDevise(bookingBody, device)
    }

    override suspend fun returnDevice() {
        //TODO
/*
        val device = localData.getDeviceInfo()
        remoteData.returnDevice(device)
*/
    }

    override suspend fun updateDevice() {
        //TODO
/*
        val device = localData.getDeviceInfo()
        remoteData.updateDevice(device)
*/
    }

    override suspend fun initDevice() {
        //TODO
        /*
        val device = localData.getDeviceInfo()
        remoteData.initDevice(device)*/
    }






}