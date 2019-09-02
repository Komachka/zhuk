package com.kstorozh.dataimpl

import com.kstorozh.dataimpl.model.BookingParam
import com.kstorozh.dataimpl.model.DeviceParam
import com.kstorozh.dataimpl.model.BookingSessionData
import com.kstorozh.dataimpl.model.RepoResult

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun updateDevice(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun takeDevice(bookingParam: BookingParam): RepoResult<Boolean>
    suspend fun returnDevice(bookingParam: BookingParam): RepoResult<Boolean>
    suspend fun deviceAlreadyInited(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun getBookingSession(): RepoResult<BookingSessionData>
    suspend fun bookDevice(bookingParam: BookingParam): RepoResult<Boolean>
    suspend fun deleteBooking(bookingId: Int, userId: String): RepoResult<Boolean>
    suspend fun editBooking(mapBookingParam: BookingParam): RepoResult<Boolean>
    suspend fun editCurrentBooking(startDate: String, endDate: String): RepoResult<Boolean>
    suspend fun getDeviceInfo(): RepoResult<DeviceParam>
    suspend fun saveNote(note: String): RepoResult<Boolean>
    suspend fun sendReport(state: String, msg: String): RepoResult<Boolean>
}