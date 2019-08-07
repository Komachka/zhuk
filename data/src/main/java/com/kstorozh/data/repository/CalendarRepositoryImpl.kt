package com.kstorozh.data.repository

import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.createError
import com.kstorozh.dataimpl.CalendarRepository
import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.RepoResult
import org.koin.core.KoinComponent

internal class CalendarRepositoryImpl(
    private val remoteData: RemoteData,
    private val bookingDataMapper: BookingDataMapper
) : CalendarRepository, KoinComponent {
    override suspend fun getBookingByDate(startDate: String, endDate: String): RepoResult<CalendarBookingData> {
        val repoResult: RepoResult<CalendarBookingData> = RepoResult()
        return when (val result = remoteData.getBookingByDate(startDate, endDate)) {
            is ApiResult.Success -> {
                repoResult.apply {
                    data = bookingDataMapper.mapBookingDataToCalendarData(result.data)
                }
            }
            is ApiResult.Error<*> -> {
                val koin = this as KoinComponent
                repoResult.apply {
                    data = null
                    error = createError(Endpoints.INIT_DEVICE, result, koin)
                }
            }
        }
    }
}