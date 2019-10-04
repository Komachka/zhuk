package com.evo.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class UserData(

    @SerializedName("user_id")
    @Expose
    var userId: Int
)

internal data class LoginUserResponse(

    @SerializedName("data")
    @Expose
    val data: UserData
) : BaseResponse()