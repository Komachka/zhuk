package com.evo.domain.mapper

import com.evo.dataimpl.model.NearbyBooking
import com.evo.dataimpl.model.CalendarBookingData
import com.evo.dataimpl.model.CalendarDay
import com.evo.domainapi.model.Booking
import com.evo.domainapi.model.BookingInfo
import com.evo.domainapi.model.NearbyDomainBooking
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

        val foramtter = ISODateTimeFormat.dateTimeParser()
        val start = foramtter.parseDateTime(calendarDay.startDate)
        val end = foramtter.parseDateTime(calendarDay.endDate)

        return Booking(
            calendarDay.bookingId,
            calendarDay.userId, calendarDay.slackUsername,
            start.millis,
            end.millis,
            calendarDay.duration
        )
    }

    fun mapToNearbyBooking(nearbyBooking: NearbyBooking): NearbyDomainBooking {
        val foramtter = ISODateTimeFormat.dateTimeParser()
        val start = foramtter.parseDateTime(nearbyBooking.startDate)
        val end = foramtter.parseDateTime(nearbyBooking.endDate)
        return NearbyDomainBooking(
            nearbyBooking.id,
            nearbyBooking.userName,
            nearbyBooking.userId,
            start.millis, end.millis
        )
    }
}