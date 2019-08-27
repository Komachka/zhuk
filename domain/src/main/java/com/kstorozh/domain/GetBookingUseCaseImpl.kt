package com.kstorozh.domain

import com.kstorozh.dataimpl.CalendarRepository
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.CalendarMapper
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.BookingInfo
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.DomainResult
import java.text.SimpleDateFormat

class GetBookingUseCaseImpl(
    private val bookingRepository: CalendarRepository,
    private val repository: DeviseRepository,
    private val errorMapper: ErrorMapper,
    val deviceMapper: DeviceInfoMapper,
    val mapper: CalendarMapper
) : GetBookingUseCase {

    override suspend fun loadBooking(startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val dateFormat = SimpleDateFormat(DAY_MONTH_YEAR_FORMAT)
        val repoResult = bookingRepository.getBookingByDate(
            dateFormat.format(startDate), dateFormat.format(endDate))
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapCalendarBookingDataToBooking(it) }
        return DomainResult(data, domainError)
    }

    override suspend fun createBooking(bookingInputData: BookingInputData, startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val repoResult = repository.bookDevice(deviceMapper.mapBookingParam(bookingInputData, bookingInputData.startDate))
        repoResult.data?.let {
            if (it)
                return loadBooking(startDate, endDate)
        }
        return DomainResult(null, DomainErrors(message = BOOKING_NOT_CREATED))
    }

    override suspend fun deleteBooking(bookingId: Int, userId: String, startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val repoResult = repository.deleteBooking(bookingId, userId)
        repoResult.data?.let {
            if (it)
                return loadBooking(startDate, endDate)
        }
        return DomainResult(null, DomainErrors(message = BOOKING_NOT_DELETED))
    }

    override suspend fun editBooking(bookingInputData: BookingInputData, bookingId: Int, startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val repoResult = repository.editBooking(deviceMapper.mapBookingParam(bookingInputData, bookingInputData.startDate, bookingId.toString()))
        repoResult.data?.let {
            if (it)
                return loadBooking(startDate, endDate)
        }
        return DomainResult(null, DomainErrors(message = BOOKING_NOT_EDITED))
    }
}