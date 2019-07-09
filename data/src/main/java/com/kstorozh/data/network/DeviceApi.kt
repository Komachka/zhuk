package com.kstorozh.data.network

import INIT_DEVISE_URL
import RETURN_DEVISE_URL
import TAKE_DEVISE_URL
import UPDATE_DEVISE_URL
import com.kstorozh.data.BookingBody
import com.kstorozh.data.Device
import com.kstorozh.data.StatusBody
import retrofit2.http.*

internal interface DeviceApi {
    @POST(INIT_DEVISE_URL)
    fun initDevice(@Body device: Device)

    @PUT(UPDATE_DEVISE_URL)
    fun updateDevice(
        @Body device: Device,
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