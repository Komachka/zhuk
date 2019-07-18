package com.kstorozh.data.models

import com.google.gson.annotations.SerializedName

internal class ReturnDeviceBody(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("device_id") val deviceId: Int,
    @SerializedName("is_active") val isActive: Boolean = false
)