package com.evo.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class DeleteBookingBody(
    @Expose
    @SerializedName("user_id")
    val userId: String,

    @Expose
    @SerializedName("device_id")
    val deviceId: String
)