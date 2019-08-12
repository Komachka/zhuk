package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.BookingInfo
import com.kstorozh.domainapi.model.DomainResult

interface GetBookingUseCase {
    suspend fun loadBooking(startDate: Long, endDate: Long): DomainResult<BookingInfo>
}