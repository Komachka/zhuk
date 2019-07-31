package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class DeviceData(

    @SerializedName("device_id")
    @Expose
    val deviceId: Int
)

internal data class InitDeviceResponse(

    @SerializedName("data")
    @Expose
    val data: DeviceData
) : BaseResponse()
