package com.kstorozh.data.repository

import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.ReturnDeviceBody
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam

internal class DeviceDataMapper {

    fun mapDeviceData(deviceParam: DeviceParam): Device = Device(
        "0",
        deviceParam.uid,
        deviceParam.model,
        deviceParam.os,
        deviceParam.osVersion,
        deviceParam.memory,
        deviceParam.storage)

    fun mapBookingDeviceInfo(bookingParam: BookingParam, deviceId: String, isActive: Boolean = true) = BookingBody(
        1,
        bookingParam.userId,
        deviceId.toInt(),
        bookingParam.startDate,
        bookingParam.endDate,
        isActive = isActive,
        isForce = bookingParam.isForce)

    fun mapBookingParamForReturn(bookingParam: BookingParam, id: String) = ReturnDeviceBody(bookingParam.userId.toInt(), id.toInt())
}