package com.kstorozh.data.network

import INIT_DEVISE_URL
import RETURN_DEVISE_URL
import TAKE_DEVISE_URL
import UPDATE_DEVISE_URL
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.InitDeviceResponse
import com.kstorozh.data.models.StatusBody
import retrofit2.Response

import retrofit2.http.*

internal interface DeviceApi {
    @POST(INIT_DEVISE_URL)
    fun initDevice(@Body device: Device): Response<InitDeviceResponse>

    @PUT(UPDATE_DEVISE_URL)
    fun updateDevice(
        @Body deviceUpdate: Device,
        @Path("id") deviceId: String
    )

    @POST(TAKE_DEVISE_URL)
    fun takeDevice(
        @Body bookingBody: BookingBody
    )

    @PUT(RETURN_DEVISE_URL)
    fun returnDevice(
        @Body status: StatusBody,
        @Path("id") deviceId: String
    )
}