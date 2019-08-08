package com.kstorozh.domain

import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.Booking

class GetBookingUseCaseImpl : GetBookingUseCase {
    override fun loadBooking(startDate: Long, endDate: Long, onLoad: (List<Booking>) -> Unit) {
    }
}