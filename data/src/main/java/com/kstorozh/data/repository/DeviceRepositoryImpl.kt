package com.kstorozh.data.repository

import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.network.RemoteData
import com.kstorozh.dataimpl.model.BookingParam
import com.kstorozh.dataimpl.model.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper
) : DeviseRepository {
    override suspend fun initDevice(deviceParam: DeviceParam) {
        val device = mapper.mapDeviceData(deviceParam)
        remoteData.initDevice(device)

        // TODO remoteData.initDevice(device) return 'device_id': if OK add to db
    }

    override suspend fun updateDevice(deviceParam: DeviceParam) {
        remoteData.updateDevice(mapper.mapDeviceData(deviceParam), deviceId = deviceParam.uid)
        // TODO if OK update  in db
    }

    override suspend fun takeDevice(bookingParam: BookingParam) {

        remoteData.takeDevise(mapper.mapBookingDeviceInfo(bookingParam), bookingParam.deviceId.toString())
    }

    override suspend fun returnDevice(deviceParam: DeviceParam) {
        remoteData.returnDevice(deviceParam.uid)
    }
}