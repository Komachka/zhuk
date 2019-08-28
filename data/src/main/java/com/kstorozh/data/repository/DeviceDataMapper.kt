package com.kstorozh.data.repository

import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.DeleteBookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.ReturnDeviceBody
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam

internal class DeviceDataMapper {

    fun mapDeviceData(deviceParam: DeviceParam): Device = Device(
        "0",
        deviceParam.uid,
        deviceParam.uid, // mac address
        deviceParam.model,
        deviceParam.os,
        deviceParam.osVersion,
        deviceParam.memory,
        deviceParam.storage,
        deviceParam.storageEmpty,
        deviceParam.note)

    fun mapBookingDeviceInfo(bookingParam: BookingParam, deviceId: String, isActive: Boolean = true) = BookingBody(
        1,
        bookingParam.userId,
        deviceId.toInt(),
        bookingParam.startDate,
        bookingParam.endDate,
        isActive = isActive,
        isForce = bookingParam.isForce)

    fun mapBookingParamForReturn(bookingParam: BookingParam, id: String) = ReturnDeviceBody(bookingParam.userId.toInt(), id.toInt())

    fun mapToDeleteBookingModel(userId: String, deviceId: String) = DeleteBookingBody(userId, deviceId)

    fun mapDeviceInfo(device: Device) = DeviceParam(
        device.uid,
        device.model,
        device.os,
        device.osVersion,
        device.memory,
        device.storage,
        device.storageEmpty,
        device.note
    )
}