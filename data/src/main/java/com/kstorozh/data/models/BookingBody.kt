package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class BookingBody(
    @Expose @SerializedName("user_id") val userId: Int,
    @Expose @SerializedName("device_id") val deviceId: Int,
    @Expose @SerializedName("start_date") val startDate: String,
    @Expose @SerializedName("end_date") val endDate: String,
    @Expose @SerializedName("is_active") val isActive: Boolean

)
