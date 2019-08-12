package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.CalendarDay
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.domainapi.model.BookingInfo
import org.joda.time.format.ISODateTimeFormat

class CalendarMapper {
    fun mapCalendarBookingDataToBooking(calendarBookingData: CalendarBookingData): BookingInfo {
        val map: Map<String, List<Booking>> = mapMapOfCalendarBookingData(calendarBookingData.dayData)
        val duration = calendarBookingData.slotDuration
        return BookingInfo(map, duration)
    }

    private fun mapMapOfCalendarBookingData(dayData: Map<String, List<CalendarDay>>): Map<String, List<Booking>> {
        val bookinfDayInfo = mutableMapOf<String, List<Booking>>()
        dayData.forEach { (key, value) ->
            val list = mutableListOf<Booking>()
            value.forEach {
                list.add(mapToBooking(it))
            }
            bookinfDayInfo[key] = list
        }
        return bookinfDayInfo
    }

    private fun mapToBooking(calendarDay: CalendarDay): Booking {

        val foramtter = ISODateTimeFormat.dateTimeNoMillis()
        val start = foramtter.parseDateTime(calendarDay.startDate)
        val end = foramtter.parseDateTime(calendarDay.endDate)

        return Booking(
            calendarDay.bookingId,
            calendarDay.userId, calendarDay.slackUsername,
            start.millis, // TODO need to parse this date
            end.millis,
            calendarDay.duration
        )
    }
}