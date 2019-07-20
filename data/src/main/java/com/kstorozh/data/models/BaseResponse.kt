package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class Errors(

    @SerializedName("field_name")
    @Expose
    val fieldName: String
)

internal open class BaseResponse(
    @SerializedName("msg")
    @Expose
    val msg: String? = null,

    @SerializedName("errors")
    @Expose
    val errors: Errors ? = null
)
