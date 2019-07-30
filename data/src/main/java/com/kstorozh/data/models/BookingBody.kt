package com.kstorozh.data.models

import BOOKING_TABLE_NAME
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = BOOKING_TABLE_NAME)
internal data class BookingBody(
    @PrimaryKey var id: String,

    @Expose
    @SerializedName("user_id")
    val userId: String,

    @Expose
    @SerializedName("device_id")
    val deviceId: Int,

    @SerializedName("start_date")
    val startDate: String,

    @Expose
    @SerializedName("end_date")
    val endDate: String,

    @Expose
    @SerializedName("is_active")
    val isActive: Boolean

)
