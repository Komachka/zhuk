package com.kstorozh.data.repository

import com.kstorozh.data.models.BookingDataByDay
import com.kstorozh.data.models.Day
import com.kstorozh.dataimpl.model.out.CalendarBookingData
import com.kstorozh.dataimpl.model.out.CalendarDay

internal class BookingDataMapper {

    fun mapBookingDataToCalendarData(bookingDataByDay: BookingDataByDay): CalendarBookingData {
        val days = bookingDataByDay.dayData
        return CalendarBookingData(bookingDataByDay.slotDuration, mapToCalendarDay(days))
    }

    private fun mapToCalendarDay(days: Map<String, List<Day>>): Map<String, List<CalendarDay>> {
        return HashMap<String, List<CalendarDay>>().apply {
            days.forEach { (key, value) ->
                val list = mutableListOf<CalendarDay>()
                value.forEach { day ->
                    list.add(
                        CalendarDay(
                            day.id,
                            day.userId,
                            day.slackUsername,
                            day.startDate,
                            day.endDate,
                            day.duration
                        )
                    )
                }
                this[key] = list
            }
        }
    }
}