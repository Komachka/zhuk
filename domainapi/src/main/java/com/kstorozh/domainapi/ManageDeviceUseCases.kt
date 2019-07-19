package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData

interface ManageDeviceUseCases {

    suspend fun initDevice(deviceInputData: DeviceInputData): Boolean

    suspend fun takeDevice(bookingParam: BookingInputData): Boolean

    suspend fun returnDevice(bookingParam: BookingInputData): Boolean
}