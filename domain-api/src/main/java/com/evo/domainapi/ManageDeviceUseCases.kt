package com.evo.domainapi

import com.evo.domainapi.model.BookingInputData
import com.evo.domainapi.model.DeviceInputData
import com.evo.domainapi.model.DomainResult
import com.evo.domainapi.model.SessionData

interface ManageDeviceUseCases {

    suspend fun initDevice(deviceInputData: DeviceInputData): DomainResult<Boolean>
    suspend fun takeDevice(bookingParam: BookingInputData): DomainResult<Boolean>
    suspend fun returnDevice(bookingParam: BookingInputData): DomainResult<Boolean>
    suspend fun getSession(): DomainResult<SessionData>
    suspend fun isDeviceInited(deviceInputData: DeviceInputData): DomainResult<Boolean>
    suspend fun editCurrentBooking(endDate: Long): DomainResult<Boolean>
    suspend fun sendReport(state: String, msg: String): DomainResult<Boolean>
}