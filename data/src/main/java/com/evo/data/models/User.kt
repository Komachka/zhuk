package com.evo.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class User(

    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("slack_uid")
    @Expose
    var slackUid: String,
    @SerializedName("slack_username")
    @Expose
    var slackUsername: String,
    @SerializedName("pin")
    @Expose
    var pin: String? = null,
    @SerializedName("role")
    @Expose
    var role: String? = null
)

internal data class UsersData(

    @SerializedName("data")
    @Expose
    val users: List<User>
) : BaseResponse()
