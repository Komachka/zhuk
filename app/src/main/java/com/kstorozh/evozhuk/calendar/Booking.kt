package com.kstorozh.evozhuk.calendar

import java.util.*

class Booking(
    val isMyBooking: Boolean,
    val isOtherBooking: Boolean,
    val calendar: Calendar,
    val slackName: String,
    val startDate: String,
    val endDate: String
)