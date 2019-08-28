package com.kstorozh.domain.mapper

import android.annotation.SuppressLint
import com.kstorozh.dataimpl.model.into.BookingParam
import com.kstorozh.dataimpl.model.into.DeviceParam
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.SessionData
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
    fun mapBookingParam(bookingInputData: BookingInputData, startDate: Calendar? = null, bookingId: String? = null): BookingParam {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+03:00'")
        return BookingParam(
            bookingId = bookingId,
            userId = bookingInputData.userId,
            startDate = startDate?.let { format.format(startDate.time) } ?: "2019-07-07T00:00:00+03:00",
            endDate = bookingInputData.endDate?.let { format.format(bookingInputData.endDate!!.time) } ?: "2019-07-07T00:00:00+03:00"
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun mapBookingSession(bookingSession: BookingSessionData): SessionData {
        val endDateCalendar = Calendar.getInstance()
        val foramtter = ISODateTimeFormat.dateTimeParser()
        val end = foramtter.parseDateTime(bookingSession.endDate)
        endDateCalendar.time = Date(end.millis)
        return SessionData(bookingSession.userId, endDateCalendar)
    }
}