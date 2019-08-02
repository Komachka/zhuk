package com.kstorozh.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.Token

@Database(entities = [Device::class, Token::class, BookingBody::class], version = 3)

internal abstract class DeviceDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    abstract fun tokenDao(): TokenDao

    abstract fun bookingDao(): BookingDao
}