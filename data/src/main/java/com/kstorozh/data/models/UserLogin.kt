package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class UserLogin(
    @Expose @SerializedName("slack_username") val userName: String,
    @Expose @SerializedName("pin") val pin: String
)