package com.evo.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class BookingDataByDay(

    @SerializedName("slot_duration")
    @Expose
    val slotDuration: Long,
    @SerializedName("data")
    @Expose
    val dayData: Map<String, List<Day>>
)

internal data class DayData(

    @Expose
    var dayData: Map<String, List<Day>>
)

internal data class Day(

    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("user_id")
    @Expose
    var userId: Int,
    @SerializedName("slack_username")
    @Expose
    var slackUsername: String,
    @SerializedName("start_date")
    @Expose
    var startDate: String,
    @SerializedName("end_date")
    @Expose
    var endDate: String,
    @Expose
    var duration: Long
)