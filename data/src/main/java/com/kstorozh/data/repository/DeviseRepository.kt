package com.kstorozh.data.repository

import com.kstorozh.data.BookingBody

interface DeviseRepository {
    suspend fun initDevice()
    suspend fun updateDevice()
    suspend fun takeDevice(bookingBody: BookingBody)
    suspend fun returnDevice()


}