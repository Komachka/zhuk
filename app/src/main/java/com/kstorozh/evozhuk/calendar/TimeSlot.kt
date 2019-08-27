package com.kstorozh.evozhuk.calendar

import com.kstorozh.domainapi.model.Booking
import java.util.*

class TimeSlot(
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