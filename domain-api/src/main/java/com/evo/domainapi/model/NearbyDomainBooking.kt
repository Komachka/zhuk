package com.evo.domainapi.model

data class NearbyDomainBooking(
    val id: Int,
    val userName: String,
    val userId: Int,
    val startDate: Long,
    val endDate: Long
)