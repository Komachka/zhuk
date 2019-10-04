package com.evo.dataimpl.model

data class CalendarBookingData(
    val slotDuration: Long,
    val dayData: Map<String, List<CalendarDay>>
)