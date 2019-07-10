package com.kstorozh.data.repository

import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.dataimpl.model.BookingParam
import com.kstorozh.dataimpl.model.DeviceParam

internal class DeviceDataMapper {

    fun mapDeviceData(deviceParam: DeviceParam): Device = Device(
        "0",
        deviceParam.uid,
        deviceParam.model,
        deviceParam.os,
        deviceParam.osVersion,
        deviceParam.memory,
        deviceParam.storage)

    fun mapBookingDeviceInfo(bookingParam: BookingParam): BookingBody = BookingBody(bookingParam.pin,
        bookingParam.userId,
        bookingParam.deviceId,
        bookingParam.startDate,
        bookingParam.endDate)
}