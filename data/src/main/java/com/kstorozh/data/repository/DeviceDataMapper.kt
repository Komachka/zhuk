package com.kstorozh.data.repository

import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.DeviceUpdate
import com.kstorozh.data.models.User
import com.kstorozh.dataimpl.BookingParam
import com.kstorozh.dataimpl.DeviceParam
import com.kstorozh.dataimpl.UserParam

internal class DeviceDataMapper {

    fun mapDeviceData(deviceParam: DeviceParam): Device = Device(deviceParam.uid,
        deviceParam.model,
        deviceParam.os,
        deviceParam.osVersion,
        deviceParam.memory,
        deviceParam.storage)

    fun mapDeviceForUpdate(deviceParam: DeviceParam): DeviceUpdate = DeviceUpdate(deviceParam.os,
        deviceParam.osVersion)

    fun mapBookingDeviceInfo(bookingParam: BookingParam): BookingBody = BookingBody(bookingParam.pin,
        bookingParam.userId,
        bookingParam.deviceId,
        bookingParam.startDate,
        bookingParam.endDate)

    fun mapUserParam(userParam: UserParam): User = User(userParam.id,
        userParam.slackId,
        userParam.slackUserName,
        userParam.pin)
}