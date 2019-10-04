package com.evo.domain.mapper

import android.annotation.SuppressLint
import com.evo.dataimpl.model.BookingParam
import com.evo.dataimpl.model.DeviceParam
import com.evo.dataimpl.model.BookingSessionData
import com.evo.domainapi.model.BookingInputData
import com.evo.domainapi.model.DeviceInfo
import com.evo.domainapi.model.DeviceInputData
import com.evo.domainapi.model.SessionData
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.text.DecimalFormat

import java.util.*

const val MEMORY_DECIMAL_FORMAT = "#.##"
class DeviceInfoMapper {

    fun mapDeviceInfoToDeviceParam(deviceInputData: DeviceInputData) =
        DeviceParam(
            deviceInputData.uid,
            deviceInputData.model,
            deviceInputData.os,
            deviceInputData.osVersion,
            deviceInputData.memory,
            deviceInputData.storage,
            deviceInputData.storageEmpty,
            deviceInputData.note
        )

    @SuppressLint("SimpleDateFormat")

    fun mapBookingParam(bookingInputData: BookingInputData, startDate: Calendar? = null, bookingId: String? = null): BookingParam {
        val foramtter = ISODateTimeFormat.dateTime()
        val start = startDate?.let { foramtter.print(it.timeInMillis) } ?: DateTime().toString()
        val end = bookingInputData.endDate?.let { foramtter.print(it.timeInMillis) } ?: DateTime().toString()
        return BookingParam(
            bookingId,
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

    private fun Long.mgToGb() = this * 0.001

    fun mapToDeviceInfo(data: DeviceParam): DeviceInfo {
        val df = DecimalFormat(MEMORY_DECIMAL_FORMAT)
        return DeviceInfo(
        data.osVersion,
        data.model,
        data.uid,
            "${df.format(data.memory.toLong().mgToGb())} Gb",
            "${df.format(data.storage.toLong().mgToGb())} Gb",
            "${df.format(data.storageEmpty.toLong().mgToGb())} Gb",
        data.note)
    }
}