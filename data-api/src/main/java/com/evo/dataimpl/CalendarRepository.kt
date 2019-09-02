package com.evo.dataimpl

import com.evo.dataimpl.model.NearbyBooking
import com.evo.dataimpl.model.CalendarBookingData
import com.evo.dataimpl.model.RepoResult

interface CalendarRepository {
    suspend fun getBookingByDate(startDate: String, endDate: String): RepoResult<CalendarBookingData>
    suspend fun getBookingFromLocal(): RepoResult<CalendarBookingData>
    suspend fun getNearbyBooking(): RepoResult<NearbyBooking>
}