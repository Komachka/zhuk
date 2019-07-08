package com.kstorozh.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "devices")
data class Device(@PrimaryKey val uid : String,
                  val model : String,
                  val os : String,
                  @ColumnInfo(name = "os_version")val osVersion:String,
                  val memory:Int)