package com.kstorozh.data.models

import com.google.gson.annotations.SerializedName

internal class UserLogin(
    @SerializedName("slack_username") val userName: String,
    @SerializedName("pin") val pin: String
)