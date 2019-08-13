package com.kstorozh.data.network

import INIT_DEVISE_URL
import RETURN_DEVISE_URL
import TAKE_DEVISE_URL
import UPDATE_DEVISE_URL
import com.kstorozh.data.models.*
import com.kstorozh.data.models.BaseResponse
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.BookingCreated
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.InitDeviceResponse
import com.kstorozh.data.models.ReturnDeviceBody
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

    @PUT(RETURN_DEVISE_URL)
    suspend fun returnDevice(
        @Body status: ReturnDeviceBody,
        @Path("id") bookingId: Int
    ): Response<BaseResponse>
}