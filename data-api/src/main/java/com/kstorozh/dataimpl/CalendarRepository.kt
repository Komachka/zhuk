package com.kstorozh.dataimpl

import com.kstorozh.dataimpl.model.NearbyBooking
import com.kstorozh.dataimpl.model.CalendarBookingData
import com.kstorozh.dataimpl.model.RepoResult

interface CalendarRepository {
    suspend fun getBookingByDate(startDate: String, endDate: String): RepoResult<CalendarBookingData>
    suspend fun getBookingFromLocal(): RepoResult<CalendarBookingData>
    suspend fun getNearbyBooking(): RepoResult<NearbyBooking>
}