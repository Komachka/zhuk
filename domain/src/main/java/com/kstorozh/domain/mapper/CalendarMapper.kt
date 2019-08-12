package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.CalendarDay
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.domainapi.model.BookingInfo

class CalendarMapper {
    fun mapCalendarBookingDataToBooking(calendarBookingData: CalendarBookingData): BookingInfo {
        val map: Map<String, Booking> = mapMapOfCalendarBookingData(calendarBookingData.dayData)
        val duration = calendarBookingData.slotDuration
        return BookingInfo(map, duration)
    }

    private fun mapMapOfCalendarBookingData(dayData: Map<String, CalendarDay>): Map<String, Booking> {
        val bookinfDayInfo = mutableMapOf<String, Booking>()
        dayData.forEach { (key, value) ->
            bookinfDayInfo[key] = mapToBooking(value)
        }
        return bookinfDayInfo
    }

    private fun mapToBooking(calendarDay: CalendarDay) =
        Booking(calendarDay.bookingId,
            calendarDay.userId, calendarDay.slackUsername,
            calendarDay.startDate.toLong(),
            calendarDay.endDate.toLong(),
            calendarDay.duration)
}