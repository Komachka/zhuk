package com.kstorozh.dataimpl.model

data class BookingParam(
    val bookingId: String? = null,
    val userId: String,
    val startDate: String,
    val endDate: String,
    val isForce: Boolean
)