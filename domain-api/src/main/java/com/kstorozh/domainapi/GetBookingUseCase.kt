package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.BookingInfo
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DomainResult

interface GetBookingUseCase {
    suspend fun loadBooking(startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun createBooking(bookingInputData: BookingInputData, startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun deleteBooking(bookingId: Int, userId: String, startDate: Long, endDate: Long): DomainResult<BookingInfo>
    suspend fun editBooking(bookingInputData: BookingInputData, bookingId: Int, startDate: Long, endDate: Long): DomainResult<BookingInfo>
}