package com.kstorozh.data.models

import com.google.gson.annotations.SerializedName

internal class BookingBody(
    val pin: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("device_id") val deviceId: Int,
    @SerializedName("start_date") val startDate: Int,
    @SerializedName("end_date") val endDate: Int
)
