package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.Exception

internal abstract class ApiErrorBody
internal data class ApiErrorWithField(

    @SerializedName("errors")
    @Expose
    val errors: Errors
) : ApiErrorBody()

internal data class ApiErrorBodyWithMessage(

    @SerializedName("msg")
    @Expose
    val msg: String
) : ApiErrorBody()

internal data class ApiErrorBodyUnexpected(val message: String) : ApiErrorBody()

internal data class ApiError(
    val body: ApiErrorBody,
    val exception: Exception
)