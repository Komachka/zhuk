package com.kstorozh.domainapi.model

data class BookingInfo(
    val bookingMap: Map<String, List<Booking>>,
    val duration: Long
)