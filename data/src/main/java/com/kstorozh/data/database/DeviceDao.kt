package com.kstorozh.data.database

import androidx.room.*
import com.kstorozh.data.Device

@Dao
interface DeviceDao {

    @Query("SELECT * FROM devices LIMIT 1")
    fun getDeviceInfo() : List<Device>

    @Insert
    fun insertDevice(device: Device) : Long

    @Update
    fun updateDevice(device: Device) : Boolean

    @Query("DELETE FROM devices")
    fun deleteAllDeviceInfo()

}