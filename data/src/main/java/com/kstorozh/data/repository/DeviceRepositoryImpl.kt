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
) : DeviseRepository, KoinComponent {

    private val myError: MutableLiveData<MyError> = MutableLiveData()

    override suspend fun getErrors(): MutableLiveData<MyError> {
        return myError
    }

    override suspend fun deviceAlreadyInited(deviceParam: DeviceParam): Boolean {

        val device = mapper.mapDeviceData(deviceParam)
        val res = tokenRepository.getToken()?.let { true } ?: false
        Log.d(LOG_TAG, "is device inited in DeviceRepositoryImpl -  ${tokenRepository.getToken()}")
        return res
    }

    override suspend fun getBookingSession(): BookingSessionData? {
        val device = localData.getDeviceInfo()
        Log.d(LOG_TAG, "Device info $device")
        val booking = localData.getBookingByDeviceId(device.id)
        return if (booking != null) { BookingSessionData(booking.userId, booking.endDate) } else null
    }

    override suspend fun initDevice(deviceParam: DeviceParam): Boolean {
        val device = mapper.mapDeviceData(deviceParam)

        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.INIT_DEVICE, result, this))
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
                myError.postValue(createError(Endpoints.UPDATE_DEVICE, result, this))
                false
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): Boolean {

        val device = localData.getDeviceInfo()
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
                myError.postValue(createError(Endpoints.TAKE_DEVICE, result, this))
                false
            }
        }
    }

    override suspend fun returnDevice(bookingParam: BookingParam): Boolean {
        val device = localData.getDeviceInfo()
        return when (val result = remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id))) {
            is ApiResult.Success -> {
                localData.deleteBookingInfo()
                true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.RETURN_DEVICE, result, this))
                false
            }
        }
    }
}