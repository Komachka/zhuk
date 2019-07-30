package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.dataimpl.DataError
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

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val tokenRepository: TokenRepository
) : DeviseRepository {

    override suspend fun deviceAlreadyInited(deviceParam: DeviceParam): RepoResult<Boolean> {
        val res = tokenRepository.getToken()?.let { true } ?: false
        return RepoResult(res)
    }

    override suspend fun getBookingSession(): RepoResult<BookingSessionData> {
        var bookingSessionData: BookingSessionData?
        val result:RepoResult<BookingSessionData> = RepoResult()
        localData.getDeviceInfo()?.let { device ->
            localData.getBookingByDeviceId(device.id)?.let {
                bookingSessionData = BookingSessionData(it.userId, it.endDate)
                result.data  = bookingSessionData
            }
        }
        return result
    }

    override suspend fun initDevice(deviceParam: DeviceParam): RepoResult<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val repoResult:RepoResult<Boolean> = RepoResult()
        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                Log.d(LOG_TAG, "device id ${device.id}")
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                repoResult.data = true
                repoResult
            }
            is ApiResult.Error<*> -> {
                repoResult.data = false
                repoResult.error = createError(Endpoints.INIT_DEVICE, result)
                repoResult
            }
        }
    }

    override suspend fun updateDevice(deviceParam: DeviceParam): RepoResult<Boolean> {
        val device = mapper.mapDeviceData(deviceParam) // TODO this device need to take from db
        val repoResult:RepoResult<Boolean> = RepoResult()
        return when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                // TODO handle bd error
                localData.updateDevice(device)
                repoResult.data = true
                repoResult
            }
            is ApiResult.Error<*> -> {
                repoResult.data = false
                repoResult.error = createError(Endpoints.UPDATE_DEVICE, result)
                repoResult
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): RepoResult<Boolean> {

        val device = localData.getDeviceInfo()
        val repoResult:RepoResult<Boolean> = RepoResult()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id)
            return when (val result = remoteData.takeDevise(
                bookingBody,
                device.id
            )) {
                is ApiResult.Success -> {
                    localData.saveBooking(bookingBody)
                    Log.d(LOG_TAG, "save booking to db $bookingBody")
                    repoResult.data = true
                    repoResult
                }
                is ApiResult.Error<*> -> {
                    Log.d(LOG_TAG, "Taking device." + result.errorResponse!!.code())
                    repoResult.data = false
                    repoResult.error = createError(Endpoints.TAKE_DEVICE, result)
                    repoResult
                }
            }
        }
        repoResult.data = false
         // TODO make function create error create not only api errors repoResult.error = createError(Endpoints.INIT_DEVICE, )
        return repoResult
    }

    override suspend fun returnDevice(bookingParam: BookingParam): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> = RepoResult()
        device?.let {
            return when (val result =
                remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id))) {
                is ApiResult.Success -> {
                    localData.deleteBookingInfo()
                    repoResult.data = true
                    repoResult
                }
                is ApiResult.Error<*> -> {
                    repoResult.data = false
                    repoResult.error = createError(Endpoints.RETURN_DEVICE, result)
                    repoResult
                }
            }
        }
        repoResult.data = false
        // TODO make function create error create not only api errors repoResult.error = createError(Endpoints.INIT_DEVICE, )
        return repoResult
    }
}