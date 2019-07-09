package com.kstorozh.data.repository

import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.network.RemoteData
import com.kstorozh.dataimpl.BookingParam
import com.kstorozh.dataimpl.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper
) : DeviseRepository {
    override suspend fun initDevice(deviceParam: DeviceParam) {
        remoteData.initDevice(mapper.mapDeviceData(deviceParam))
        // TODO if OK add to db
    }

    override suspend fun updateDevice(deviceParam: DeviceParam) {
        remoteData.updateDevice(mapper.mapDeviceForUpdate(deviceParam), deviceId = deviceParam.uid)
        // TODO if OK update  in db
    }

    override suspend fun takeDevice(bookingParam: BookingParam) {

        remoteData.takeDevise(mapper.mapBookingDeviceInfo(bookingParam), bookingParam.deviceId.toString())
    }

    override suspend fun returnDevice(deviceParam: DeviceParam) {
        remoteData.returnDevice(deviceParam.uid)
    }
}