package com.kstorozh.data.network

import END_DATE
import GET_BOOKING_URL
import START_DATE
import com.kstorozh.data.models.BookingDataByDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CalendarApi {
    @GET(GET_BOOKING_URL)
    suspend fun getBookingByDate(@Query(START_DATE)start_date: String, @Query(END_DATE)end_date: String):
            Response<BookingDataByDay>
}