package com.evo.data.network

import EDIT_BOOKING_URL
import INIT_DEVISE_URL
import REPORT_URL
import RETURN_DEVISE_URL
import TAKE_DEVISE_URL
import UPDATE_DEVISE_URL
import com.evo.data.models.*
import com.evo.data.models.BaseResponse
import com.evo.data.models.BookingBody
import com.evo.data.models.BookingCreated
import com.evo.data.models.Device
import com.evo.data.models.InitDeviceResponse
import com.evo.data.models.ReturnDeviceBody
import retrofit2.Response

import retrofit2.http.*

internal interface DeviceApi {
    @POST(INIT_DEVISE_URL)
    suspend fun initDevice(@Body device: Device): Response<InitDeviceResponse>

    @PUT(UPDATE_DEVISE_URL)
    suspend fun updateDevice(
        @Body deviceUpdate: Device,
        @Path("id") deviceId: String
    ): Response<BaseResponse>

    @POST(TAKE_DEVISE_URL)
    suspend fun takeDevice(
        @Body bookingBody: BookingBody
    ): Response<BookingCreated>

    @POST(RETURN_DEVISE_URL)
    suspend fun returnDevice(
        @Body status: ReturnDeviceBody,
        @Path("id") bookingId: Int
    ): Response<BaseResponse>

    @PUT(EDIT_BOOKING_URL)
    suspend fun editBooking(
        @Body status: BookingBody,
        @Path("id") bookingId: Int
    ): Response<BaseResponse>

    @POST(REPORT_URL)
    suspend fun sendReport(
        @Body report: Report
    ): Response<BaseResponse>
}