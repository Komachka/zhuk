package com.kstorozh.evozhuk.calendar

import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.FIRST_HOUR
import com.kstorozh.evozhuk.LAST_HOUR
import com.kstorozh.evozhuk.ONE_SECOND
import com.kstorozh.evozhuk.utils.getStringHourMinuteDate
import org.joda.time.DateTime

interface BookingParser {

    fun createEmptySlots(dateInMilisec: Long, stepInMiliSeconds: Long): List<TimeSlot> {
        val listOfTimeSlot = mutableListOf<TimeSlot>()
        val sdt = DateTime(dateInMilisec).withHourOfDay(FIRST_HOUR)
        val edt = DateTime(dateInMilisec).withHourOfDay(LAST_HOUR)
        var iSec = sdt.millis
        while (iSec <= edt.millis) {
            val tmpStartDate = DateTime(iSec)
            val tmpEndDate = DateTime((iSec + stepInMiliSeconds))
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
                    range = (iSec until iSec + stepInMiliSeconds)
                )
            )
            iSec += stepInMiliSeconds
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
            var time = book.duration * ONE_SECOND - step
            while (time / step > 0.0) {
                val startDateTmp = dateTimeStart.millis + ((time / step) * step)
                val dateTimeStartTmp = DateTime(startDateTmp)
                val listWithRange = this.filter { it.range.contains(dateTimeStartTmp.millis) }
                if (listWithRange.isNotEmpty()) {
                    listWithRange.first().fill(
                        isContinueFlag = true,
                        book = book,
                        userId = userId
                    )
                }
                time -= step
            }
        }
    }

    fun TimeSlot.fill(isContinueFlag: Boolean, dateTimeStart: DateTime? = null, dateTimeEnd: DateTime? = null, book: Booking, userId: Int) {
        apply {
            booking = book
            isContinue = isContinueFlag
            dateTimeEnd?.let { slotEndDate = it.getStringHourMinuteDate() }
            dateTimeStart?.let { slotStartDate = it.getStringHourMinuteDate() }
            if (booking!!.userId == userId) isMyBooking = true
            else isOtherBooking = true
        }
    }
}