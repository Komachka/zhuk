package com.evo.data.models

import com.google.gson.annotations.Expose

data class Report(

    @Expose
    val user_id: Int? = null,

    @Expose
    val type: String,

    @Expose
    val text: String,

    @Expose
    val device_id: String
)