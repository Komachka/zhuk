package com.kstorozh.dataimpl

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.model.out.BookingSessionData

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam): Boolean
    suspend fun updateDevice(deviceParam: DeviceParam): Boolean
    suspend fun takeDevice(bookingParam: BookingParam): Boolean
    suspend fun returnDevice(bookingParam: BookingParam): Boolean
    suspend fun getErrors(): MutableLiveData<MyError>
    suspend fun deviceAlreadyInited(deviceParam: DeviceParam): Boolean
    suspend fun getBookingSession(): BookingSessionData?
}