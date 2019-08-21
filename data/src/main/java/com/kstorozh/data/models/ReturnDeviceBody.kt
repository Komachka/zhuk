package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class ReturnDeviceBody(
    @Expose @SerializedName("user_id") val userId: Int,
    @Expose @SerializedName("device_id") val deviceId: Int
)