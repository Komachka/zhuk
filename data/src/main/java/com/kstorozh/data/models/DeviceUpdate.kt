package com.kstorozh.data.models

import com.google.gson.annotations.SerializedName

class DeviceUpdate(
    val os: String,
    @SerializedName("version")val osVersion: String
)