package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.Booking

interface GetBookingUseCase {
    fun loadBooking(startDate: Long, endDate: Long, onLoad: (List<Booking>) -> Unit)
}