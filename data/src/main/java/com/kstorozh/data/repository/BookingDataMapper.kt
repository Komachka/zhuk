package com.kstorozh.data.repository

import com.kstorozh.data.models.BookingDataByDay
import com.kstorozh.data.models.Day
import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.CalendarDay

internal class BookingDataMapper {

    fun mapBookingDataToCalendarData(bookingDataByDay: BookingDataByDay): CalendarBookingData {
        val days = bookingDataByDay.data.dayData
        return CalendarBookingData(bookingDataByDay.slotDuration, mapToCalendarDay(days))
    }

    private fun mapToCalendarDay(days: Map<String, Day>): Map<String, CalendarDay> {
        return HashMap<String, CalendarDay>().apply {
            days.forEach { (key, value) ->
                this[key] = CalendarDay(
                    value.id,
                    value.slackUsername,
                    value.startDate,
                    value.endDate,
                    value.duration) }
        }
    }
}