package com.kstorozh.evozhuk.calendar

import com.kstorozh.domainapi.model.Booking
import java.util.*

class TimeSlot(
    var isMyBooking: Boolean,
    var isOtherBooking: Boolean,
    var slotStartDate: String,
    var slotEndDate: String,
    var booking:Booking? = null
)