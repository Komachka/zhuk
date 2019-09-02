package com.kstorozh.dataimpl.model

data class CalendarBookingData(
    val slotDuration: Long,
    val dayData: Map<String, List<CalendarDay>>
)