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

    fun mapBookingDeviceInfo(bookingParam: BookingParam, deviceId: String) = BookingBody(
        bookingParam.userId,
        deviceId.toInt(),
        bookingParam.startDate,
        bookingParam.endDate,
        isActive = true)

    fun mapBookingParamForReturn(bookingParam: BookingParam, id: String) = ReturnDeviceBody(bookingParam.userId, id.toInt())
}