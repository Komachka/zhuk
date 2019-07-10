package com.kstorozh.data.models

import DEVICE_INFO_TABLE_NAME
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = DEVICE_INFO_TABLE_NAME)
internal data class Device(
    @Expose @PrimaryKey var id: String,
    val uid: String,
    val model: String,
    val os: String,
    @ColumnInfo(name = "os_version")val osVersion: String,
    val memory: Int,
    val storage: Int
)

data class Data(val device_id: String)

data class InitDeviceResponse(
    val message: String,
    val data: Data
)
