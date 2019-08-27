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

class Booking {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("device_id")
    @Expose
    var deviceId: Int? = null
    @SerializedName("start_date")
    @Expose
    var startDate: String? = null
    @SerializedName("end_date")
    @Expose
    var endDate: String? = null
    @SerializedName("is_active")
    @Expose
    var isActive: Boolean? = null

}