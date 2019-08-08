package com.kstorozh.domainapi.model

class Booking(
    val id: Int,
    val userId: Int,
    val slackUserName: String,
    val startDate: Long,
    val endDate: Long,
    val duration: Long
)