package com.kstorozh.domain.mapper

import android.annotation.SuppressLint
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.SessionData
import java.text.SimpleDateFormat

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

    @SuppressLint("SimpleDateFormat")
    fun mapBookingParam(bookingInputData: BookingInputData, startDate: Calendar? = null): BookingParam {

        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return BookingParam(
            bookingInputData.userId,
            startDate?.let { format.format(startDate.time) } ?: "2019-07-07 00:00:00",
            bookingInputData.endDate?.let { format.format(bookingInputData.endDate!!.time) } ?: "2019-07-07 00:00:00"
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun mapBookingSession(bookingSession: BookingSessionData): SessionData {
        val endDateCalendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        endDateCalendar.setTime(format.parse(bookingSession.endDate)!!)
        return SessionData(bookingSession.userId, endDateCalendar)
    }
}