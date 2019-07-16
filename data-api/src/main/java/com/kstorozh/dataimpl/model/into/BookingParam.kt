package com.kstorozh.dataimpl.model.into

data class BookingParam(
    val pin: Int,
    val userId: Int,
    val deviceId: Int,
    val startDate: Int,
    val endDate: Int
)