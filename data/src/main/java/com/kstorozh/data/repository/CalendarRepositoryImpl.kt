package com.kstorozh.data.repository

import BOOKING_DATA_EMPTY_ERROR
import com.kstorozh.data.BookingsStorage
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.createError
import com.kstorozh.dataimpl.CalendarRepository
import com.kstorozh.dataimpl.DataError
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.RepoResult
import org.koin.core.KoinComponent
import java.lang.NullPointerException

internal class CalendarRepositoryImpl(
    private val remoteData: RemoteData,
    private val bookingDataMapper: BookingDataMapper,
    private val bookingStorage:BookingsStorage
) : CalendarRepository, KoinComponent {
    override suspend fun getBookingFromLocal(): RepoResult<CalendarBookingData> {
        val repoResult: RepoResult<CalendarBookingData> = RepoResult()
        bookingStorage.data?.let {
            repoResult.data = it
            return repoResult
        }
        repoResult.apply {
            data = null
            error = DataError(ErrorStatus.UNEXPECTED_ERROR, "Booking in local storage is not exists", NullPointerException("Booking in local storage is not exists"))
        }
        return repoResult
    }

    override suspend fun getBookingByDate(startDate: String, endDate: String): RepoResult<CalendarBookingData> {
        val repoResult: RepoResult<CalendarBookingData> = RepoResult()
        return when (val result = remoteData.getBookingByDate(startDate, endDate)) {
            is ApiResult.Success -> {
                repoResult.apply {
                    try {
                        data = bookingDataMapper.mapBookingDataToCalendarData(result.data)
                        bookingStorage.data = data
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