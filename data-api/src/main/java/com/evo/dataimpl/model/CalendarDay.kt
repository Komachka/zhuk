package com.evo.dataimpl.model

data class CalendarDay(
    val bookingId: Int,
    val userId: Int,
    val slackUsername: String,
    val startDate: String,
    val endDate: String,
    val duration: Long
)