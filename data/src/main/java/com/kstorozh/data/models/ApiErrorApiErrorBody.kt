package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal abstract class ApiErrorBody
internal data class ApiErrorWithField(

    @SerializedName("errors")
    @Expose
    val errors: Errors
)

internal data class ApiErrorBodyWithMessage(

    @SerializedName("msg")
    @Expose
    val msg: String
)

internal data class ApiErrorBodyUnexpected(val message: String) : ApiErrorBody()
