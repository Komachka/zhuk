package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.network.TokenRepository
import com.kstorozh.data.utils.*
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.dataimpl.model.out.RepoResult
import org.koin.core.KoinComponent

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val tokenRepository: TokenRepository
) : DeviseRepository, KoinComponent {

    override suspend fun deviceAlreadyInited(deviceParam: DeviceParam): RepoResult<Boolean> {
        val res = tokenRepository.getToken()?.let { true } ?: false
        return RepoResult(res)
    }

    override suspend fun getBookingSession(): RepoResult<BookingSessionData> {
        var bookingSessionData: BookingSessionData?
        val result: RepoResult<BookingSessionData> = RepoResult()
        localData.getDeviceInfo()?.let { device ->
            localData.getBookingByDeviceId(device.id)?.let {
                bookingSessionData = BookingSessionData(it.userId, it.endDate)
                result.data = bookingSessionData
            }
        }
        return result
    }

    override suspend fun initDevice(deviceParam: DeviceParam): RepoResult<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val repoResult: RepoResult<Boolean> = RepoResult()
        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                repoResult.data = true
                repoResult
            }
            is ApiResult.Error<*> -> {
                repoResult.data = false
                repoResult.error = createError(Endpoints.INIT_DEVICE, result, this)
                repoResult
            }
        }
    }

    override suspend fun updateDevice(deviceParam: DeviceParam): RepoResult<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val repoResult: RepoResult<Boolean> = RepoResult()
        return when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                localData.updateDevice(device)
                repoResult.data = true
                repoResult
            }
            is ApiResult.Error<*> -> {
                repoResult.data = false
                repoResult.error = createError(Endpoints.UPDATE_DEVICE, result, this)
                repoResult
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): RepoResult<Boolean> {

        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> = RepoResult()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id)
            return when (val result = remoteData.takeDevise(
                bookingBody,
                device.id
            )) {
                is ApiResult.Success -> {
                    bookingBody.id = result.data.data.bookingId // TODO добавить проверку на bookinId != null
                    Log.d(LOG_TAG, "Booking id " + bookingBody.id.toString())
                    localData.saveBooking(bookingBody)
                    repoResult.data = true
                    repoResult
                }
                is ApiResult.Error<*> -> {
                    repoResult.data = false
                    repoResult.error = createError(Endpoints.TAKE_DEVICE, result, this)
                    repoResult
                }
            }
        }
        repoResult.data = false
        return repoResult
    }

    override suspend fun bookDevice(bookingParam: BookingParam): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> = RepoResult()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id, isActive = false)
            Log.d(LOG_TAG, " in method bookingparam $bookingParam")
            Log.d(LOG_TAG, "bookingBody $bookingBody")
            return when (val result = remoteData.takeDevise(
                bookingBody,
                device.id
            )) {
                is ApiResult.Success -> {
                    bookingBody.id = result.data.data.bookingId
                    Log.d(LOG_TAG, "Booking id " + bookingBody.id.toString())
                    repoResult.data = true
                    repoResult
                }
                is ApiResult.Error<*> -> {
                    repoResult.data = false
                    repoResult.error = createError(Endpoints.TAKE_DEVICE, result, this)
                    repoResult
                }
            }
        }
        repoResult.data = false
        return repoResult
    }

    override suspend fun returnDevice(bookingParam: BookingParam): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val booking = localData.getBookingByDeviceId(device!!.id)
        val repoResult: RepoResult<Boolean> = RepoResult()
        device?.let {
            return when (val result =
                remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id), booking!!.id)) {
                is ApiResult.Success -> {
                    localData.deleteBookingInfo()
                    repoResult.data = true
                    repoResult
                }
                is ApiResult.Error<*> -> {
                    repoResult.data = false
                    repoResult.error = createError(Endpoints.RETURN_DEVICE, result, this)
                    repoResult
                }
            }
        }
    }
}