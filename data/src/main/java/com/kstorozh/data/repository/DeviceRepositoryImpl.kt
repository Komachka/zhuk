package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.dataimpl.MyError
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.*
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository
import org.koin.core.KoinComponent

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val tokenRepository: TokenRepository
) : DeviseRepository, KoinComponent {


    private var myError: MyError? = null

    override suspend fun getErrors(): MyError? {
        return myError
    }


    override suspend fun deviceAlreadyInited(deviceParam: DeviceParam): Boolean {

        val device = mapper.mapDeviceData(deviceParam)
        val res =  tokenRepository.getToken()?.let {true} ?: false
        Log.d(LOG_TAG, "is device inited in DeviceRepositoryImpl -  ${tokenRepository.getToken()}")
        return res

    }



    override suspend fun initDevice(deviceParam: DeviceParam): Boolean {
        val device = mapper.mapDeviceData(deviceParam)

        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                // TODO add error handling from bd and check if data exists do not write
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                true
            }
            is ApiResult.Error<*> -> {
                myError = createError(Endpoints.INIT_DEVICE, result, this)
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
                myError = createError(Endpoints.UPDATE_DEVICE, result, this)
                false
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): Boolean {

        val device = localData.getDeviceInfo()
        return when (val result = remoteData.takeDevise(
            mapper.mapBookingDeviceInfo(bookingParam, device.id),
            device.id
        )) {
            is ApiResult.Success -> {
                true
            }
            is ApiResult.Error<*> -> {
                myError = createError(Endpoints.TAKE_DEVICE, result, this)
                false
            }
        }
    }

    override suspend fun returnDevice(bookingParam: BookingParam): Boolean {
        val device = localData.getDeviceInfo()
        return when (val result = remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id))) {
            is ApiResult.Success -> {
                true
            }
            is ApiResult.Error<*> -> {
                myError = createError(Endpoints.RETURN_DEVICE, result, this)
                false
            }
        }
    }
}