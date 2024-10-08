package com.evo.domain

import com.evo.dataimpl.CalendarRepository
import com.evo.dataimpl.DeviseRepository
import com.evo.domain.mapper.CalendarMapper
import com.evo.domain.mapper.DeviceInfoMapper
import com.evo.domain.mapper.ErrorMapper
import com.evo.domainapi.GetBookingUseCase
import com.evo.domainapi.model.*
import java.text.SimpleDateFormat

class GetBookingUseCaseImpl(
    private val bookingRepository: CalendarRepository,
    private val repository: DeviseRepository,
    private val errorMapper: ErrorMapper,
    val deviceMapper: DeviceInfoMapper,
    val mapper: CalendarMapper
) : GetBookingUseCase {

    override suspend fun getNearbyBooking(): DomainResult<NearbyDomainBooking> {
        val repoResult = bookingRepository.getNearbyBooking()
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapToNearbyBooking(it) }
        return DomainResult(data, domainError)
    }

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
            if (it) {
                return getUpdatedBookingData(startDate, endDate)
            }
        }
        return DomainResult(null, DomainErrors(message = BOOKING_NOT_CREATED))
    }

    override suspend fun deleteBooking(bookingId: Int, userId: String, startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val repoResult = repository.deleteBooking(bookingId, userId)
        repoResult.data?.let {
            if (it) {
                return getUpdatedBookingData(startDate, endDate)
            }
        }
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(null, domainError)
    }

    override suspend fun editBooking(bookingInputData: BookingInputData, bookingId: Int, startDate: Long, endDate: Long): DomainResult<BookingInfo> {
        val repoResult = repository
            .editBooking(deviceMapper.mapBookingParam(bookingInputData, bookingInputData.startDate, bookingId.toString()))
        repoResult.data?.let {
            if (it) {
                return getUpdatedBookingData(startDate, endDate)
            }
        }
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(null, domainError)
    }
}