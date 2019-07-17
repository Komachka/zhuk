package com.kstorozh.dataimpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam

interface DeviseRepository {
    suspend fun initDevice(deviceParam: DeviceParam): LiveData<Boolean>
    suspend fun updateDevice(deviceParam: DeviceParam): LiveData<Boolean>
    suspend fun takeDevice(bookingParam: BookingParam): LiveData<Boolean>
    suspend fun returnDevice(bookingParam: BookingParam): LiveData<Boolean>
    suspend fun getErrors(): MutableLiveData<MyErrors<*>>
}