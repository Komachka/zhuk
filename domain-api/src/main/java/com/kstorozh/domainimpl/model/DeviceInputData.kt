package com.kstorozh.domainimpl.model

data class DeviceInputData(
    val uid: String,
    val model: String,
    val os: String,
    val osVersion: String,
    val memory: Int,
    val storage: Int
)