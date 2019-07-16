package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.models.ApiError
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.TokenRepository
import com.kstorozh.dataimpl.model.BookingParam
import com.kstorozh.dataimpl.model.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.data.utils.parse

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val apiError: MutableLiveData<ApiError>,
    private val tokenRepository: TokenRepository
) : DeviseRepository {

    override suspend fun initDevice(deviceParam: DeviceParam) {
        val device = mapper.mapDeviceData(deviceParam)

        when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }

    override suspend fun updateDevice(deviceParam: DeviceParam) {
        val device = mapper.mapDeviceData(deviceParam)
        when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                localData.updateDevice(device)
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam) {

        when (val result = remoteData.takeDevise(mapper.mapBookingDeviceInfo(bookingParam), bookingParam.deviceId.toString())) {
            is ApiResult.Success -> {
                // TODO update livedata
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }

    override suspend fun returnDevice(deviceParam: DeviceParam) {
        when (val result = remoteData.returnDevice(deviceParam.uid)) {
            is ApiResult.Success -> {
                // TODO update livedata
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }
}