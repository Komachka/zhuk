package com.kstorozh.data.models

import com.google.gson.annotations.SerializedName

internal class StatusBody(@SerializedName("is_active") val isActive: Boolean = false)