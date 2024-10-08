package com.evo.data.repository

import DEVICE_INFO_CACHE_EMPTY_ERROR
import LOG_TAG
import android.util.Log
import com.evo.data.database.LocalDataStorage
import com.evo.data.models.ApiResult
import com.evo.data.models.Report
import com.evo.data.network.Endpoints
import com.evo.data.network.RemoteData
import com.evo.data.network.TokenRepository
import com.evo.data.utils.*
import com.evo.dataimpl.DataError
import com.evo.dataimpl.model.BookingParam
import com.evo.dataimpl.model.DeviceParam
import com.evo.dataimpl.DeviseRepository
import com.evo.dataimpl.ErrorStatus
import com.evo.dataimpl.model.BookingSessionData
import com.evo.dataimpl.model.RepoResult
import org.koin.core.KoinComponent
import java.lang.Exception
import java.lang.NullPointerException

internal class DeviceRepositoryImpl(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData,
    private val mapper: DeviceDataMapper,
    private val tokenRepository: TokenRepository
) : DeviseRepository, KoinComponent {

    override suspend fun sendReport(state: String, msg: String): RepoResult<Boolean> {
        var user: Int? = null
        var deviceId = "0"
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        localData.getDeviceInfo()?.let { device ->
            deviceId = device.id
            localData.getBookingByDeviceId(device.id)?.let {
                user = it.userId.toInt()
            }
        }
        when (val result = remoteData.sendRepo(Report(user, state, msg, deviceId))) {
            is ApiResult.Success -> {
                repoResult.data = true
            }
            is ApiResult.Error<*> -> {
                repoResult.apply {
                    data = false
                    error = createError(Endpoints.UPDATE_DEVICE, result, koin)
                }
            }
        }
        return repoResult
    }

    private val koin = this as KoinComponent

    override suspend fun saveNote(note: String): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        device?.let {
            it.note = note
            localData.insertDevice(device)
            when (val result = remoteData.initDevice(device)) {
                is ApiResult.Success -> {
                    repoResult.data = true
                }
                is ApiResult.Error<*> -> {
                    repoResult.apply {
                        data = false
                        error = createError(Endpoints.INIT_DEVICE, result, koin)
                    }
                }
            }
        }
        if (device == null) repoResult.error = DataError(ErrorStatus.UNEXPECTED_ERROR, DEVICE_INFO_CACHE_EMPTY_ERROR, Exception(DEVICE_INFO_CACHE_EMPTY_ERROR))
        return repoResult
    }

    override suspend fun getDeviceInfo(): RepoResult<DeviceParam> {
        val repoResult: RepoResult<DeviceParam> =
            RepoResult()
        localData.getDeviceInfo()?.let {
            return repoResult.apply { data = mapper.mapDeviceInfo(it) }
        }
        return repoResult.apply { error = DataError(ErrorStatus.UNEXPECTED_ERROR, DEVICE_INFO_CACHE_EMPTY_ERROR, NullPointerException(DEVICE_INFO_CACHE_EMPTY_ERROR)) }
    }

    override suspend fun deviceAlreadyInited(deviceParam: DeviceParam): RepoResult<Boolean> {
        val res = tokenRepository.getToken()?.let { true } ?: false
        return RepoResult(res)
    }

    override suspend fun getBookingSession(): RepoResult<BookingSessionData> {
        var bookingSessionData: BookingSessionData?
        val result: RepoResult<BookingSessionData> =
            RepoResult()
        localData.getDeviceInfo()?.let { device ->
            localData.getBookingByDeviceId(device.id)?.let {
                bookingSessionData =
                    BookingSessionData(it.userId, it.endDate)
                result.data = bookingSessionData
            }
        }
        return result
    }

    override suspend fun initDevice(deviceParam: DeviceParam): RepoResult<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        return when (val result = remoteData.initDevice(device)) {
            is ApiResult.Success -> {
                device.id = result.data.data.deviceId.toString()
                localData.insertDevice(device)
                tokenRepository.setToken(device.id)
                repoResult.apply {
                    data = true
                }
            }
            is ApiResult.Error<*> -> {
                repoResult.apply {
                    data = false
                    error = createError(Endpoints.INIT_DEVICE, result, koin)
                }
            }
        }
    }

    override suspend fun updateDevice(deviceParam: DeviceParam): RepoResult<Boolean> {
        val device = mapper.mapDeviceData(deviceParam)
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        return when (val result = remoteData.updateDevice(device, deviceParam.uid)) {
            is ApiResult.Success -> {
                localData.updateDevice(device)
                repoResult.apply {
                    data = true
                }
            }
            is ApiResult.Error<*> -> {
                repoResult.apply {
                    data = false
                    error = createError(Endpoints.UPDATE_DEVICE, result, koin)
                }
            }
        }
    }

    override suspend fun takeDevice(bookingParam: BookingParam): RepoResult<Boolean> {

        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id)
            return when (val result = remoteData.takeDevise(
                bookingBody,
                device.id
            )) {
                is ApiResult.Success -> {
                    bookingBody.id = result.data.data.bookingId
                    localData.saveBooking(bookingBody)
                    repoResult.apply {
                        data = true
                    }
                }
                is ApiResult.Error<*> -> {
                    repoResult.apply {
                        data = false

                        error = createError(Endpoints.TAKE_DEVICE, result, koin)
                    }
                }
            }
        }
        repoResult.data = false
        return repoResult
    }

    override suspend fun editCurrentBooking(startdate: String, endDate: String): RepoResult<Boolean> {
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        localData.getDeviceInfo()?.let { device ->
            val currentBooking = localData.getBookingByDeviceId(device.id)
            currentBooking?.let { bookingBody ->
                currentBooking.endDate = endDate
                currentBooking.startDate = startdate
                currentBooking.isForce = null
                return when (val result = remoteData.editBooking(
                    bookingBody,
                    bookingBody.id.toString()
                )) {
                    is ApiResult.Success -> {
                        localData.saveBooking(bookingBody)
                        repoResult.apply {
                            data = true
                        }
                    }
                    is ApiResult.Error<*> -> {
                        repoResult.apply {
                            data = false
                            error = createError(Endpoints.TAKE_DEVICE, result, koin)
                        }
                    }
                }
            }
        }
        return repoResult.apply { data = false }
    }

    override suspend fun bookDevice(bookingParam: BookingParam): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id, isActive = false)
            return when (val result = remoteData.takeDevise(
                bookingBody,
                device.id
            )) {
                is ApiResult.Success -> {
                    bookingBody.id = result.data.data.bookingId
                    repoResult.apply {
                        data = true
                    }
                }
                is ApiResult.Error<*> -> {
                    repoResult.apply {
                        data = false
                        error = createError(Endpoints.TAKE_DEVICE, result, koin)
                    }
                }
            }
        }
        repoResult.data = false
        return repoResult
    }

    override suspend fun editBooking(bookingParam: BookingParam): RepoResult<Boolean> {
        Log.d(LOG_TAG, bookingParam.toString())
        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        device?.let {
            val bookingBody = mapper.mapBookingDeviceInfo(bookingParam, device.id, isActive = false)
            bookingBody.isForce = null
            return when (val result = remoteData.editBooking(
                bookingBody,
                bookingParam.bookingId!!
            )) {
                is ApiResult.Success -> {
                    repoResult.apply {
                        data = true
                    }
                }
                is ApiResult.Error<*> -> {
                    repoResult.apply {
                        data = false
                        error = createError(Endpoints.TAKE_DEVICE, result, koin)
                    }
                }
            }
        }
        repoResult.data = false
        return repoResult
    }

    override suspend fun deleteBooking(bookingId: Int, userId: String): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        device?.let {
            val deleteBookingBody = mapper.mapToDeleteBookingModel(userId, device.id)
            return when (val result = remoteData.deleteBooking(
                deleteBookingBody,
                bookingId
            )) {
                is ApiResult.Success -> {
                    repoResult.apply {
                        data = true
                    }
                }
                is ApiResult.Error<*> -> {
                    repoResult.apply {
                        data = false
                        error = createError(Endpoints.TAKE_DEVICE, result, koin)
                    }
                }
            }
        }
        repoResult.data = false
        return repoResult
    }

    override suspend fun returnDevice(bookingParam: BookingParam): RepoResult<Boolean> {
        val device = localData.getDeviceInfo()
        val booking = localData.getBookingByDeviceId(device!!.id)
        val repoResult: RepoResult<Boolean> =
            RepoResult()
        device?.let {
            return when (val result =
                remoteData.returnDevice(mapper.mapBookingParamForReturn(bookingParam, device.id), booking!!.id)) {
                is ApiResult.Success -> {
                    localData.deleteBookingInfo()
                    repoResult.apply {
                        data = true
                    }
                }
                is ApiResult.Error<*> -> {
                    repoResult.apply {
                        data = false
                        error = createError(Endpoints.RETURN_DEVICE, result, koin)
                    }
                }
            }
        }
    }
}