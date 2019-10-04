package com.evo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evo.data.models.BookingBody
import com.evo.data.models.Device
import com.evo.data.models.Token

@Database(entities = [Device::class, Token::class, BookingBody::class], version = 3)

internal abstract class DeviceDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    abstract fun tokenDao(): TokenDao

    abstract fun bookingDao(): BookingDao
}