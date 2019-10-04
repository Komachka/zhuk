package com.evo.data.repository

import BOOKING_CACHE_EMPTY_ERROR
import BOOKING_DATA_EMPTY_ERROR
import com.evo.data.BookingsCache
import com.evo.data.database.LocalDataStorage
import com.evo.data.models.ApiResult
import com.evo.data.network.Endpoints
import com.evo.data.network.RemoteData
import com.evo.data.utils.createError
import com.evo.dataimpl.CalendarRepository
import com.evo.dataimpl.DataError
import com.evo.dataimpl.ErrorStatus
import com.evo.dataimpl.model.NearbyBooking
import com.evo.dataimpl.model.CalendarBookingData
import com.evo.dataimpl.model.RepoResult
import org.koin.core.KoinComponent
import java.lang.Exception
import java.lang.NullPointerException

internal class CalendarRepositoryImpl(
    private val remoteData: RemoteData,
    private val bookingDataMapper: BookingDataMapper,
    private val bookingStorage: BookingsCache,
    private val localDataStorage: LocalDataStorage
) : CalendarRepository, KoinComponent {

    override suspend fun getNearbyBooking(): RepoResult<NearbyBooking> {
        val device = localDataStorage.getDeviceInfo()
        val repoResult: RepoResult<NearbyBooking> =
            RepoResult()
        device?.let {
            return when (val result = remoteData.getNearbyBooking(device.id)) {
                is ApiResult.Success -> {
                    repoResult.apply {
                        result.data.data?.booking?.let {
                            data = bookingDataMapper.maptonearbyBooking(it)
                        }
                    }
                }
                is ApiResult.Error<*> -> {
                    val koin = this as KoinComponent
                    repoResult.apply {
                        data = null
                        error = createError(Endpoints.GET_BOOKING, result, koin)
                    }
                }
            }
        }
        return repoResult
    }

    override suspend fun getBookingFromLocal(): RepoResult<CalendarBookingData> {
        val repoResult: RepoResult<CalendarBookingData> =
            RepoResult()
        bookingStorage.data.let {
            try {
                repoResult.data = it[0]
                return repoResult
            } catch (e: Exception) {}
        }
        repoResult.apply {
            data = null
            error = DataError(ErrorStatus.UNEXPECTED_ERROR, BOOKING_CACHE_EMPTY_ERROR, NullPointerException(BOOKING_CACHE_EMPTY_ERROR))
        }
        return repoResult
    }

    override suspend fun getBookingByDate(startDate: String, endDate: String): RepoResult<CalendarBookingData> {
        val repoResult: RepoResult<CalendarBookingData> =
            RepoResult()
        return when (val result = remoteData.getBookingByDate(startDate, endDate)) {
            is ApiResult.Success -> {
                repoResult.apply {
                    try {
                        data = bookingDataMapper.mapBookingDataToCalendarData(result.data)

                        data?.let {
                            bookingStorage.data.clear()
                            bookingStorage.data.add(0, it)
                        }
                    } catch (e: Throwable) {
                        data = null
                        error = DataError(ErrorStatus.UNEXPECTED_ERROR, BOOKING_DATA_EMPTY_ERROR, e)
                    }
                }
            }
            is ApiResult.Error<*> -> {
                val koin = this as KoinComponent
                repoResult.apply {
                    data = null
                    error = createError(Endpoints.GET_BOOKING, result, koin)
                }
            }
        }
    }
}