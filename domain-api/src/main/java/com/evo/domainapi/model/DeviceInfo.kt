package com.evo.domainapi.model

data class DeviceInfo(
    val version: String,
    val model: String,
    val id: String,
    val memory: String,
    val storage: String,
    val storageEmpty: String,
    val note: String
)