package com.kstorozh.data.repository

import com.kstorozh.data.models.Booking
import com.kstorozh.data.models.BookingDataByDay
import com.kstorozh.data.models.Day
import com.kstorozh.dataimpl.model.NearbyBooking
import com.kstorozh.dataimpl.model.CalendarBookingData
import com.kstorozh.dataimpl.model.CalendarDay

internal class BookingDataMapper {

    fun mapBookingDataToCalendarData(bookingDataByDay: BookingDataByDay): CalendarBookingData {
        val days = bookingDataByDay.dayData
        return CalendarBookingData(
            bookingDataByDay.slotDuration,
            mapToCalendarDay(days)
        )
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

    fun maptonearbyBooking(booking: Booking) = NearbyBooking(
        booking.id,
        booking.userId,
        booking.userName,
        booking.startDate,
        booking.endDate,
        booking.isActive
    )
}