package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.dataimpl.MyError
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.network.TokenRepository
import com.kstorozh.data.utils.*
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.model.out.BookingSessionData

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val tokenRepository: TokenRepository
) : DeviseRepository {

    private val myError: MutableLiveData<MyError> = MutableLiveData()

    override suspend fun getErrors(): MutableLiveData<MyError> {
        return myError
    }

    override suspend fun deviceAlreadyInited(deviceParam: DeviceParam): Boolean {
        val res = tokenRepository.getToken()?.let { true } ?: false
        return res
    }

    override suspend fun getBookingSession(): BookingSessionData? {
        var bookingSessionData: BookingSessionData? = null
        val device = localData.getDeviceInfo()?.let { device ->
            val booking = localData.getBookingByDeviceId(device.id)?.let {
                bookingSessionData = BookingSessionData(it.userId, it.endDate)
            }
        }
        return bookingSessionData
    }

    override suspend fun initDevice(deviceParam: DeviceParam): Boolean {
        val device = mapper.mapDeviceData(deviceParam)

        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                Log.d(LOG_TAG, "device id ${device.id}")
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.INIT_DEVICE, result))
                false
            }
        }
    }

    override suspend fun updateDevice(deviceParam: DeviceParam): Boolean {
        val device = mapper.mapDeviceData(deviceParam) // TODO this device need to take from db
        return when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                // TODO handle bd error
                localData.updateDevice(device)
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.UPDATE_DEVICE, result))
                false
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): Boolean {

        val device = localData.getDeviceInfo()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id)
            return when (val result = remoteData.takeDevise(
                bookingBody,
                device.id
            )) {
                is ApiResult.Success -> {
                    localData.saveBooking(bookingBody)
                    Log.d(LOG_TAG, "save booking to db $bookingBody")
                    true
                }
                is ApiResult.Error<*> -> {
                    Log.d(LOG_TAG, "Taking device." + result.errorResponse!!.code())
                    myError.postValue(createError(Endpoints.TAKE_DEVICE, result))
                    false
                }
            }
        }
        return false
    }

    override suspend fun returnDevice(bookingParam: BookingParam): Boolean {
        val device = localData.getDeviceInfo()
        device?.let {
            return when (val result = remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id))) {
                is ApiResult.Success -> {
                    localData.deleteBookingInfo()
                    true
                }
                is ApiResult.Error<*> -> {
                    myError.postValue(createError(Endpoints.RETURN_DEVICE, result))
                    false
                }
            }
        }
        return false
    }
}