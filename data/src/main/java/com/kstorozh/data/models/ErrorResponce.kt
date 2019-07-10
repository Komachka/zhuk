package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class Errors(

    @SerializedName("field_name")
    @Expose
    val fieldName: String
)

internal class ErrorResponce(

    @SerializedName("errors")
    @Expose
    val errors: Errors
)

internal class MessageResponce(
    @SerializedName("msg")
    @Expose
    val msg: String
)