package com.kstorozh.data.database

import BOOKING_TABLE_NAME
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kstorozh.data.models.BookingBody
import org.jetbrains.annotations.NotNull

@Dao
internal interface BookingDao {

    @Query("SELECT * FROM $BOOKING_TABLE_NAME LIMIT 1")
    suspend fun getBookingInfo(): BookingBody

    @Query("SELECT * FROM $BOOKING_TABLE_NAME WHERE deviceId LIKE :id LIMIT 1")
    suspend fun getBookingInfoByDeviceId(@NotNull id: Int): BookingBody?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(bookingBody: BookingBody): Long

    @Query("DELETE FROM $BOOKING_TABLE_NAME")
    suspend fun deleteAllBooking()
}