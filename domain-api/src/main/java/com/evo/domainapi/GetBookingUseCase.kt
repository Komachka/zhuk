package com.evo.domainapi

import com.evo.domainapi.model.*

interface GetBookingUseCase {
    suspend fun getUpdatedBookingData(startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun createBooking(bookingInputData: BookingInputData, startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun getBookingLocal(): DomainResult<BookingInfo>
    suspend fun deleteBooking(bookingId: Int, userId: String, startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun editBooking(bookingInputData: BookingInputData, bookingId: Int, startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun getNearbyBooking(): DomainResult<NearbyDomainBooking>
}