package com.kstorozh.evozhuk.chooseTime

import java.util.*

class TimeUtils {
    companion object {

        val dateFormat = "HH:mm dd MMMM"
        fun getCurrentTimeZone(): TimeZone {
            return TimeZone.getTimeZone("Europe/Kiev")
        }

        fun getTimeInMsFromHours(hour: Int): Long {
            return System.currentTimeMillis() + 3600000L * hour
        }

        fun getTimeInMsForEndOfWorkDay(): Long {
            val currentTime = GregorianCalendar.getInstance()
            val mCalendar = GregorianCalendar(currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH), 19, 0, 0)
            mCalendar.timeZone = TimeUtils.getCurrentTimeZone()
            return mCalendar.timeInMillis
        }
    }
}