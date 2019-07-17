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

    override suspend fun initDevice(deviceParam: DeviceParam): MutableLiveData<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val mutableLiveData = MutableLiveData<Boolean>()

        when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                // TODO add error handling from bd
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                mutableLiveData.postValue(true)
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                mutableLiveData.postValue(false)
            }
        }
        return mutableLiveData
    }


    override suspend fun updateDevice(deviceParam: DeviceParam): MutableLiveData<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val mutableLiveData = MutableLiveData<Boolean>()
        when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                // TODO handle bd error
                localData.updateDevice(device)
                mutableLiveData.postValue(true)
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                mutableLiveData.postValue(false)
            }
        }
        return mutableLiveData
    }

    override suspend fun takeDevice(bookingParam: BookingParam): MutableLiveData<Boolean> {

        val device = localData.getDeviceInfo()
        val mutableLiveData = MutableLiveData<Boolean>()
        when (val result = remoteData.takeDevise(
            mapper.mapBookingDeviceInfo(bookingParam, device.id),
            device.id
        )) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(true)
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                mutableLiveData.postValue(false)
            }
        }
        return mutableLiveData
    }

    override suspend fun returnDevice(bookingParam: BookingParam): MutableLiveData<Boolean> {
        val device = localData.getDeviceInfo()
        val mutableLiveData = MutableLiveData<Boolean>()
        when (val result = remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id))) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(true)
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                mutableLiveData.postValue(false)
            }
        }
        return mutableLiveData
    }
}