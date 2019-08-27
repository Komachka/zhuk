package com.kstorozh.dataimpl.model.into

data class BookingParam(
    val bookingId: String? = null,
    val userId: String,
    val startDate: String,
    val endDate: String
)