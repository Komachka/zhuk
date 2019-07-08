package com.kstorozh.data.network

import com.kstorozh.data.Device
import retrofit2.http.*

interface DeviceApiService {
    @POST("/devices")
    fun initDevice(@Body device: Device)


    @PUT("/devices/:{id}")
    fun updateDevice(@Body device: Device, @Path("id") deviceId: String, @Header("X-ACCESS-TOKEN") token : String)


    @POST("/booking")
    fun takeDevice(@Body device: Device, @Header("X-ACCESS-TOKEN") token : String)

    @PUT("/devices/:{id}")
    fun returnDevice(@Body device: Device, @Path("id") deviceId: String, @Header("X-ACCESS-TOKEN") token : String)

}