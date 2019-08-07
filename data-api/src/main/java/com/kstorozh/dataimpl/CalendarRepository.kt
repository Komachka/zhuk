package com.kstorozh.dataimpl

import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.RepoResult

interface CalendarRepository {
    suspend fun getBookingByDate(startDate: String, endDate: String): RepoResult<CalendarBookingData>
}