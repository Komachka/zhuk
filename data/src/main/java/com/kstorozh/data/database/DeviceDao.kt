package com.kstorozh.data.database

import DEVICE_INFO_TABLE_NAME
import androidx.room.*
import com.kstorozh.data.Device

@Dao
interface DeviceDao {

    @Query("SELECT * FROM $DEVICE_INFO_TABLE_NAME LIMIT 1")
    fun getDeviceInfo(): List<Device>

    @Insert
    fun insertDevice(device: Device): Long

    @Update
    fun updateDevice(device: Device): Boolean

    @Query("DELETE FROM $DEVICE_INFO_TABLE_NAME")
    fun deleteAllDeviceInfo()
}