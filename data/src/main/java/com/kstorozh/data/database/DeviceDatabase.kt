package com.kstorozh.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kstorozh.data.models.Device

@Database(entities = [Device::class], version = 1)

internal abstract class DeviceDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    /*companion object {
        @Volatile
        private var INSTANCE: DeviceDatabase? = null

        fun getDatabase(context: Context): DeviceDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DeviceDatabase::class.java,
                    DEVICE_INFO_DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }*/
}