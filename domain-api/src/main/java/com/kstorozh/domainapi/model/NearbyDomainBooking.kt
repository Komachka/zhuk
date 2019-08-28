package com.kstorozh.domainapi.model

data class NearbyDomainBooking(
    val id: Int,
    val deviceId: Int,
    val userId: Int,
    val startDate: Long,
    val endDate: Long
)