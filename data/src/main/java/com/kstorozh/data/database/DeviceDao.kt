package com.kstorozh.data.database

import DEVICE_INFO_TABLE_NAME
import androidx.room.*
import com.kstorozh.data.models.Device

@Dao
internal interface DeviceDao {

    @Query("SELECT * FROM $DEVICE_INFO_TABLE_NAME LIMIT 1")
    suspend fun getDeviceInfo(): Device

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device): Long

    @Update
    suspend fun updateDevice(device: Device): Int

    @Query("DELETE FROM $DEVICE_INFO_TABLE_NAME")
    suspend fun deleteAllDeviceInfo()
}