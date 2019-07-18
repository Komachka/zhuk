package com.kstorozh.domainapi

import androidx.lifecycle.LiveData
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData

interface ManageDeviceUseCases {

    fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean>

    fun takeDevice(bookingParam: BookingInputData): LiveData<Boolean>

    fun returnDevice(bookingParam: BookingInputData): LiveData<Boolean>
}