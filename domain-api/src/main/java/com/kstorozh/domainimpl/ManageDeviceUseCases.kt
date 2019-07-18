package com.kstorozh.domainimpl

import androidx.lifecycle.LiveData
import com.kstorozh.domainimpl.model.BookingInputData
import com.kstorozh.domainimpl.model.DeviceInputData

interface ManageDeviceUseCases {

    fun initDevice(deviceInputData: DeviceInputData): LiveData<Boolean>

    fun takeDevice(bookingParam: BookingInputData): LiveData<Boolean>

    fun returnDevice(bookingParam: BookingInputData): LiveData<Boolean>
}