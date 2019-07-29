package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.dataimpl.MyError
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.*
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.model.out.BookingSessionData
import org.koin.core.KoinComponent

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

        initDevice(deviceParam) // TODO check why cashe is saved
        val device = mapper.mapDeviceData(deviceParam)
        val res = tokenRepository.getToken()?.let {true } ?: false
        return res
    }

    override suspend fun getBookingSession(): BookingSessionData? {
        val device = localData.getDeviceInfo()
        val deviceId =  tokenRepository.getToken()
        val booking = localData.getBookingByDeviceId(deviceId!!)
        return if (booking != null) { BookingSessionData(booking.userId, booking.endDate) } else null
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

        //val device = localData.getDeviceInfo()
        val deviceId =  tokenRepository.getToken()
        val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, deviceId!!)
        return when (val result = remoteData.takeDevise(
            bookingBody,
            deviceId
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

    override suspend fun returnDevice(bookingParam: BookingParam): Boolean {
        val device = localData.getDeviceInfo()
        val deviceId =  tokenRepository.getToken()
        return when (val result = remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, deviceId!!))) {
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
}