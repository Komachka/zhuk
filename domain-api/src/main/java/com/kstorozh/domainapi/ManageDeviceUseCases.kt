package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.DomainResult
import com.kstorozh.domainapi.model.SessionData

interface ManageDeviceUseCases {

    suspend fun initDevice(deviceInputData: DeviceInputData): DomainResult<Boolean>
    suspend fun takeDevice(bookingParam: BookingInputData): DomainResult<Boolean>
    suspend fun returnDevice(bookingParam: BookingInputData): DomainResult<Boolean>
    suspend fun getSession(): DomainResult<SessionData>
    suspend fun isDeviceInited(deviceInputData: DeviceInputData): DomainResult<Boolean>
}