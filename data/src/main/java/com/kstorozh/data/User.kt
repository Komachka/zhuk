package com.kstorozh.data

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("slack_id")val slackId: String,
    @SerializedName("slack_username") val slackUserName: String,
    val pin: String
)