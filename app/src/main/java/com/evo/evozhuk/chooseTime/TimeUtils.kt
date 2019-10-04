package com.evo.evozhuk.chooseTime

import com.evo.evozhuk.HOUR_END_OF_WORK_DAY
import com.evo.evozhuk.MILISEC_IN_HOUR
import com.evo.evozhuk.TIME_ZONE
import java.util.*

class TimeUtils {
    companion object {

        fun getCurrentTimeZone(): TimeZone {
            return TimeZone.getTimeZone(TIME_ZONE)
        }

        fun getTimeInMsFromHours(hour: Int): Long {
            return System.currentTimeMillis() + MILISEC_IN_HOUR * hour
        }

        fun getTimeInMsForEndOfWorkDay(): Long {
            val currentTime = GregorianCalendar.getInstance()
            val mCalendar = GregorianCalendar(currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH), HOUR_END_OF_WORK_DAY, 0, 0)
            mCalendar.timeZone = getCurrentTimeZone()
            return mCalendar.timeInMillis
        }

        fun getTimeInMsForNextTwoMonth(): Long {
            val currentTime = GregorianCalendar.getInstance()
            currentTime.add(Calendar.MONTH, 2)
            currentTime.timeZone = getCurrentTimeZone()
            return currentTime.timeInMillis
        }
    }
}