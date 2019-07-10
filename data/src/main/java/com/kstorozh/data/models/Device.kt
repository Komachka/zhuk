package com.kstorozh.data.models

import DEVICE_INFO_TABLE_NAME
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = DEVICE_INFO_TABLE_NAME)
internal data class Device(
    @PrimaryKey var id: String,

    @Expose
    val uid: String,

    @Expose
    val model: String,

    @Expose
    val os: String,

    @ColumnInfo(name = "os_version")
    @Expose
    val osVersion: String,

    @Expose
    val memory: Int,

    @Expose
    val storage: Int
)
