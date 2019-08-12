package com.kstorozh.domain

import android.util.Log
import com.kstorozh.dataimpl.CalendarRepository
import com.kstorozh.domain.mapper.CalendarMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.BookingInfo
import com.kstorozh.domainapi.model.DomainResult

class GetBookingUseCaseImpl(
    val bookingRepository: CalendarRepository,
    val errorMapper: ErrorMapper,
    val mapper: CalendarMapper
) : GetBookingUseCase {

    override suspend fun loadBooking(startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val repoResult = bookingRepository.getBookingByDate(startDate.toString(), endDate.toString())
        Log.d("MainActivity", "result ${repoResult.data?.dayData}")
        Log.d("MainActivity", "error ${repoResult.error}")
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapCalendarBookingDataToBooking(it) }
        return DomainResult(data, domainError)
    }
}