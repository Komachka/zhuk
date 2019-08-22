package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class ApiErrorWithField(

    val errors: Errors
)

internal data class ApiErrorBodyWithError(
    @SerializedName("errors")
    @Expose
    var errors: String? = null,

    @SerializedName("msg")
    @Expose
    var msg: String? = null,

    @SerializedName("data")
    @Expose
    var data: ErrorData? = null
)

internal data class ErrorData(
    @SerializedName("booking_id")
    @Expose
    val bookingId: Int
)

internal data class ApiErrorBodyWithMsg(
    @SerializedName("msg")
    @Expose
    var errors: String? = null
)

internal data class ApiErrorBodyUnexpected(val message: String)
