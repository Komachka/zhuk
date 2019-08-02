package com.kstorozh.evozhuk.chooseTime
import com.kstorozh.evozhuk.MINUETS_IN_HOUR
import java.util.*

class CustomTime(
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var seconds: Int = 0
) {
    init {
        val dateAndTimeNow = Calendar.getInstance()
        dateAndTimeNow.timeZone = TimeUtils.getCurrentTimeZone()
        year = dateAndTimeNow.get(Calendar.YEAR)
        month = dateAndTimeNow.get(Calendar.MONTH)
        day = dateAndTimeNow.get(Calendar.DAY_OF_MONTH)
        hour = dateAndTimeNow.get(Calendar.HOUR_OF_DAY)
        minute = dateAndTimeNow.get(Calendar.MINUTE)
        seconds = dateAndTimeNow.get(Calendar.SECOND)
    }

    fun countMinutesWithTimePickerInterval(minuteInterval: Int) {
        if (minute % minuteInterval != 0) {
            val minuteFloor = minute + minuteInterval - minute % minuteInterval
            minute = minuteFloor + if (minute == minuteFloor + 1) minuteInterval else 0
            if (minute >= MINUETS_IN_HOUR) {
                minute %= MINUETS_IN_HOUR
                hour++
            }
        }
    }

    fun getMillisec(): Long {
        val calendar = GregorianCalendar(year, month, day, hour, minute, seconds)
        calendar.timeZone = TimeUtils.getCurrentTimeZone()
        return calendar.time.time
    }
}