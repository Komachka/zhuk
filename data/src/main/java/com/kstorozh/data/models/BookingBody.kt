package com.kstorozh.data.models

import BOOKING_TABLE_NAME
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = BOOKING_TABLE_NAME)
internal data class BookingBody(

    @PrimaryKey var id: Int,

    @Expose
    @SerializedName("user_id")
    val userId: String,

    @Expose
    @SerializedName("device_id")
    val deviceId: Int,

    @Expose
    @SerializedName("start_date")
    var startDate: String,

    @Expose
    @SerializedName("end_date")
    var endDate: String,

    @Expose
    @SerializedName("is_active")
    val isActive: Boolean,

    @Expose
    @SerializedName("force")
    var isForce: Boolean?

)

internal class BookingCreated(

    @Expose
    val msg: String,

    @Expose
    val data: BookingId
)

internal class BookingId(

    @SerializedName("booking_id")
    @Expose
    val bookingId: Int
)
