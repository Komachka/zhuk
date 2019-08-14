package com.kstorozh.evozhuk.calendar

import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.utils.getStringHourMinuteDate
import org.joda.time.DateTime

interface BookingParser {

    fun createEmptySlots(dateInMilisec: Long, slotDuration: Long): List<TimeSlot> {
        val listOfTimeSlot = mutableListOf<TimeSlot>()
        val sdt = DateTime(dateInMilisec).withHourOfDay(8)
        val edt = DateTime(dateInMilisec).withHourOfDay(20)
        val firstHour = sdt.millis / 1000
        val lastHour = edt.millis / 1000
        val step = slotDuration!! // in seconds
        var iSec = firstHour
        while (iSec <= lastHour) {
            val tmpStartDate = DateTime(iSec * 1000)
            val tmpEndDate = DateTime((iSec + step) * 1000)
            val startDate = tmpStartDate.getStringHourMinuteDate()
            val endDate = tmpEndDate.getStringHourMinuteDate()
            listOfTimeSlot.add(
                TimeSlot(
                    isMyBooking = false,
                    isOtherBooking = false,
                    isContinue = false,
                    timeLable = startDate,
                    slotStartDate = startDate,
                    slotEndDate = endDate,
                    range = (iSec..(iSec + step))
                )
            )
            iSec += step
        }
        return listOfTimeSlot
    }

    fun List<TimeSlot>.fillBusySlots(list: List<Booking>?, userId: Int, step: Long) {
        list?.forEach { booking ->
            val dateTimeStart = DateTime(booking.startDate)
            val dateTimeEnd = DateTime(booking.endDate)
            val r = this.filter {
                it.range.contains(dateTimeStart.millis / 1000)
            }
            if (r.isNotEmpty()) {
                val item = r.first()
                item.booking = booking
                item.isContinue = false
                item.slotEndDate = dateTimeEnd.getStringHourMinuteDate()
                item.slotStartDate = dateTimeStart.getStringHourMinuteDate()
                if (item.booking!!.userId == userId)
                    item.isMyBooking = true
                else
                    item.isOtherBooking = true
            }
            var time = booking.duration
            while (time / step > 0.0) {
                val startDateTmp = dateTimeStart.millis + ((time / step) * step * 1000)
                val dateTimeStartTmp = DateTime(startDateTmp)

                val r = this.filter {
                    it.range.contains(dateTimeStartTmp.millis / 1000)
                }
                if (r.isNotEmpty()) {
                    val item = r.first()
                    item.booking = booking
                    item.isContinue = true
                    if (item.booking!!.userId == userId)
                        item.isMyBooking = true
                    else
                        item.isOtherBooking = true
                }
                time -= step
            }
        }
    }
}