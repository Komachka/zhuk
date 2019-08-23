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

const val DAY_MONTH_YEAR_FORMAT = "dd-MM-yyyy"
class GetBookingUseCaseImpl(
    private val bookingRepository: CalendarRepository,
    private val repository: DeviseRepository,
    private val errorMapper: ErrorMapper,
    val deviceMapper: DeviceInfoMapper,
    val mapper: CalendarMapper
) : GetBookingUseCase {
    override suspend fun getBookingLocal(): DomainResult<BookingInfo> {
        val repoResult = bookingRepository.getBookingFromLocal()
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapCalendarBookingDataToBooking(it) }
        return DomainResult(data, domainError)
    }

    override suspend fun getUpdatedBookingData(startDate: Long, endDate: Long): DomainResult<BookingInfo> {
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
                return getUpdatedBookingData(startDate, endDate)
        }
        return DomainResult(null, DomainErrors(message = "Booking was not created"))
    }
}