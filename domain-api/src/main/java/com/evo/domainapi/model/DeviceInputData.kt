package com.evo.domainapi.model

data class DeviceInputData(
    val uid: String,
    val model: String,
    val os: String,
    val osVersion: String,
    val memory: Int,
    val storage: Int,
    val storageEmpty: Int,
    val note: String
)