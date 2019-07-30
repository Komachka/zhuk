package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal abstract class ApiErrorBody
internal data class ApiErrorWithField(

    val errors: Errors
)

class ApiErrorBodyWithMessage {

    @SerializedName("errors")
    @Expose
    var errors: String? = null
}

internal data class ApiErrorBodyUnexpected(val message: String) : ApiErrorBody()
