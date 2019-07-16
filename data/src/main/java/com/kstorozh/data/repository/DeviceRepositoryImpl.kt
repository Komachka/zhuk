package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.errors.ApiError
import com.kstorozh.dataimpl.MyErrors
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.TokenRepository
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.data.utils.parse

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val myError: MutableLiveData<MyErrors>,
    private val tokenRepository: TokenRepository
) : DeviseRepository {

    override suspend fun getErrors(): MutableLiveData<MyErrors> {
        return myError
    }

    override suspend fun initDevice(deviceParam: DeviceParam): Boolean {
        val device = mapper.mapDeviceData(deviceParam)

        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                // TODO add error handling from bd
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                false
            }
        }
    }

    override suspend fun updateDevice(deviceParam: DeviceParam): Boolean {
        val device = mapper.mapDeviceData(deviceParam)
        return when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                // TODO handle bd error
                localData.updateDevice(device)
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                false
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): Boolean {

        return when (val result = remoteData.takeDevise(mapper.mapBookingDeviceInfo(bookingParam), bookingParam.deviceId.toString())) {
            is ApiResult.Success -> {
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                false
            }
        }
    }

    override suspend fun returnDevice(deviceParam: DeviceParam): Boolean {
        return when (val result = remoteData.returnDevice(deviceParam.uid)) {
            is ApiResult.Success -> {
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                false
            }
        }
    }
}