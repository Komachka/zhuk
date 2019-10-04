package com.evo.evozhuk.calendar_day

import com.evo.domainapi.model.Booking

data class TimeSlot(
    var isMyBooking: Boolean,
    var isOtherBooking: Boolean,
    var isContinue: Boolean,
    var timeLable: String,
    var slotStartDate: String,
    var slotEndDate: String,
    var booking: Booking? = null,
    val range: LongRange,
    var isActive: Boolean = false
)