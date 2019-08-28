package com.kstorozh.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewarBookingResult {

    @Expose
    var msg: String? = null

    @SerializedName("data")
    @Expose
    var data: BookingData? = null
}

class BookingData {

    @SerializedName("booking")
    @Expose
    var booking: Booking? = null
}

class Booking (

    @SerializedName("booking_id")
    @Expose
    var id: Int,
    @SerializedName("booking_user_id")
    @Expose
    var userId: Int,
    @SerializedName("user_slack_username")
    @Expose
    var userName: String,
    @SerializedName("booking_start_date")
    @Expose
    var startDate: String,
    @SerializedName("booking_end_date")
    @Expose
    var endDate: String,
    @SerializedName("is_active")
    @Expose
    var isActive: Boolean
)