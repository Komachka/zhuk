package com.kstorozh.evozhuk.calendar_day

import com.kstorozh.domainapi.model.Booking
import java.util.*

data class TimeSlot(
    var isMyBooking: Boolean,
    var isOtherBooking: Boolean,
    var isContinue: Boolean,
    var timeLable: String,
    var slotStartDate: String,
    var slotEndDate: String,
    var booking: Booking? = null,
    val range: LongRange
)