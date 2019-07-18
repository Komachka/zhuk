package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.domainimpl.model.BookingInputData
import com.kstorozh.domainimpl.model.DeviceInputData

import java.util.*

class DeviceInfoMapper {

    fun mapDeviceInfoToDeviceParam(deviceInputData: DeviceInputData) =
        DeviceParam(
            deviceInputData.uid,
            deviceInputData.model,
            deviceInputData.os,
            deviceInputData.osVersion,
            deviceInputData.memory,
            deviceInputData.storage
        )

    fun mapBookingParam(bookingInputData: BookingInputData, startDate: Date? = null): BookingParam {

        return BookingParam(
            bookingInputData.userId.toInt(),
            startDate?.let { startDate.time.toInt() } ?: 0,
            bookingInputData.endDate.time.toInt()
        )
    }
}