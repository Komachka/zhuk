package com.kstorozh.domainapi.model

import java.util.*

data class BookingInputData(val userId: String, val startDate: Calendar, val endDate: Calendar?)