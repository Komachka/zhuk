package com.kstorozh.dataimpl

data class BookingParam(
    val pin: Int,
    val userId: Int,
    val deviceId: Int,
    val startDate: Int,
    val endDate: Int
)