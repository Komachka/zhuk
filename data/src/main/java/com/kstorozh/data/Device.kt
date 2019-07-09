package com.kstorozh.data

import DEVICE_INFO_TABLE_NAME
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DEVICE_INFO_TABLE_NAME)
internal data class Device(
    @PrimaryKey val uid: String,
    val model: String,
    val os: String,
    @ColumnInfo(name = "os_version")val osVersion: String,
    val memory: Int,
    val storage: Int
)