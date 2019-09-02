package com.evo.data.network

import DELETE_BOOKING_URL
import END_DATE
import GET_BOOKING_URL
import NEARBY_BOOKING_URL
import START_DATE
import com.evo.data.models.*
import com.evo.data.models.BaseResponse
import com.evo.data.models.BookingDataByDay
import com.evo.data.models.DeleteBookingBody
import com.evo.data.models.DeviceId
import retrofit2.Response
import retrofit2.http.*

internal interface CalendarApi {
    @GET(GET_BOOKING_URL)
    suspend fun getBookingByDate(@Query(START_DATE)start_date: String, @Query(END_DATE)end_date: String):
            Response<BookingDataByDay>

    @POST(DELETE_BOOKING_URL)
    suspend fun deleteBooking(
        @Body deleteBookingBody: DeleteBookingBody,
        @Path("id") bookingId: Int
    ): Response<BaseResponse>

    @POST(NEARBY_BOOKING_URL)
    suspend fun getNearBooking(
        @Body deviceId: DeviceId
    ): Response<NewarBookingResult>
}