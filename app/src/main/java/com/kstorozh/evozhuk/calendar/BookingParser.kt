package com.kstorozh.evozhuk.calendar

import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.FIRST_HOUR
import com.kstorozh.evozhuk.LAST_HOUR
import com.kstorozh.evozhuk.ONE_SECOND
import com.kstorozh.evozhuk.utils.getStringHourMinuteDate
import org.joda.time.DateTime

interface BookingParser {

    fun createEmptySlots(dateInMilisec: Long, stepInSeconds: Long): List<TimeSlot> {
        val listOfTimeSlot = mutableListOf<TimeSlot>()
        val sdt = DateTime(dateInMilisec).withHourOfDay(FIRST_HOUR)
        val edt = DateTime(dateInMilisec).withHourOfDay(LAST_HOUR)
        val firstHour = sdt.millis
        val lastHour = edt.millis
        var iSec = firstHour
        while (iSec <= lastHour) {
            val tmpStartDate = DateTime(iSec)
            val tmpEndDate = DateTime((iSec + stepInSeconds))
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
                    range = (iSec..(iSec + stepInSeconds))
                )
            )
            iSec += stepInSeconds
        }
        return listOfTimeSlot
    }

    fun List<TimeSlot>.fillBusySlots(list: List<Booking>?, userId: Int, step: Long) {
        list?.forEach { book ->
            val dateTimeStart = DateTime(book.startDate)
            val dateTimeEnd = DateTime(book.endDate)

            val listWithRange = this.filter {
                it.range.contains(dateTimeStart.millis)
            }
            if (listWithRange.isNotEmpty()) {
                listWithRange.first().fill(
                    isContinueFlag = false,
                    dateTimeStart = dateTimeStart,
                    dateTimeEnd = dateTimeEnd,
                    book = book,
                    userId = userId
                )
            }
            var time = book.duration * ONE_SECOND
            while (time / step > 0.0) {
                val startDateTmp = dateTimeStart.millis + ((time / step) * step)
                val dateTimeStartTmp = DateTime(startDateTmp)
                val endDateTmp = DateTime(startDateTmp + step)
                val listWithRange = this.filter { it.range.contains(dateTimeStartTmp.millis) }
                if (listWithRange.isNotEmpty()) {
                    listWithRange.first().fill(
                        isContinueFlag = true,
                        dateTimeStart = dateTimeStartTmp,
                        dateTimeEnd = endDateTmp,
                        book = book,
                        userId = userId
                    )
                }
                time -= step
            }
        }
    }

    fun TimeSlot.fill(isContinueFlag: Boolean, dateTimeStart: DateTime, dateTimeEnd: DateTime, book: Booking, userId: Int) {
        apply {
            booking = book
            isContinue = isContinueFlag
            slotEndDate = dateTimeEnd.getStringHourMinuteDate()
            slotStartDate = dateTimeStart.getStringHourMinuteDate()
            if (booking!!.userId == userId) isMyBooking = true
            else isOtherBooking = true
        }
    }
}