package com.kstorozh.domain

import android.util.Log
import com.kstorozh.dataimpl.CalendarRepository
import com.kstorozh.domain.mapper.CalendarMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.BookingInfo
import com.kstorozh.domainapi.model.DomainResult
import java.text.SimpleDateFormat

const val DAY_MONTH_YEAR_FORMAT = "dd-MM-yyyy"
class GetBookingUseCaseImpl(
    val bookingRepository: CalendarRepository,
    val errorMapper: ErrorMapper,
    val mapper: CalendarMapper
) : GetBookingUseCase {

    override suspend fun loadBooking(startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val dateFormat = SimpleDateFormat(DAY_MONTH_YEAR_FORMAT)

        val repoResult = bookingRepository.getBookingByDate(
            dateFormat.format(startDate), dateFormat.format(endDate))
        Log.d("MainActivity", "result ${repoResult.data?.dayData}")
        Log.d("MainActivity", "error ${repoResult.error}")
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapCalendarBookingDataToBooking(it) }
        return DomainResult(data, domainError)
    }
}