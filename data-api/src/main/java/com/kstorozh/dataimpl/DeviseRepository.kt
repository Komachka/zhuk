package com.kstorozh.dataimpl

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.dataimpl.model.out.RepoResult

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam): Boolean
    suspend fun updateDevice(deviceParam: DeviceParam): Boolean
    suspend fun takeDevice(bookingParam: BookingParam): Boolean
    suspend fun returnDevice(bookingParam: BookingParam): Boolean
    suspend fun getErrors(): MutableLiveData<DataError>
    suspend fun deviceAlreadyInited(deviceParam: DeviceParam): RepoResult<Boolean>
    suspend fun getBookingSession(): BookingSessionData?
}