package com.kstorozh.data.network

import GET_BOOKING_URL
import com.kstorozh.data.models.BookByDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface CalendarApi {
    @GET(GET_BOOKING_URL)
    suspend fun getBookingByDate(@Path("start_date")start_date: String, @Path("end_date")end_date: String):
            Response<BookByDay>
}