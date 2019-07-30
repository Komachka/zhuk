package com.kstorozh.dataimpl

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.dataimpl.model.out.RepoResult

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun updateDevice(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun takeDevice(bookingParam: BookingParam): RepoResult<Boolean>
    suspend fun returnDevice(bookingParam: BookingParam): RepoResult<Boolean>
    suspend fun getErrors(): MutableLiveData<DataError>
    suspend fun deviceAlreadyInited(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun getBookingSession(): RepoResult<BookingSessionData>
}