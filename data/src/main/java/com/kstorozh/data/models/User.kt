package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class User(

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("slack_id")
    @Expose
    val slackId: String,

    @SerializedName("slack_username")
    @Expose
    val slackUserName: String,

    @Expose(deserialize = false)
    val pin: String
)

internal class UsersData(

    @SerializedName("users")
    @Expose
    val users: List<User>
)

internal class UsersDataResponse(

    @SerializedName("data")
    @Expose
    val usersData: UsersData

)