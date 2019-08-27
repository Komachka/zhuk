package com.kstorozh.domain.mapper

import android.annotation.SuppressLint
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.SessionData
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
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
        val foramtter = ISODateTimeFormat.dateTime()
        val start =  startDate?.let { foramtter.print(it.timeInMillis) } ?: DateTime().toString()
        val end =  bookingInputData.endDate?.let { foramtter.print(it.timeInMillis) } ?: DateTime().toString()
        return BookingParam(
            bookingInputData.userId,
            start,
            end,
            bookingInputData.isForce
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun mapBookingSession(bookingSession: BookingSessionData): SessionData {
        val endDateCalendar = Calendar.getInstance()
        val foramatter = ISODateTimeFormat.dateTimeParser()
        endDateCalendar.time = foramatter.parseDateTime(bookingSession.endDate).toDate()
        return SessionData(bookingSession.userId, endDateCalendar)
    }
}