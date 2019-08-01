package com.kstorozh.evozhuk.chooseTime

import com.kstorozh.evozhuk.HOUR_END_OF_WORK_DAY
import com.kstorozh.evozhuk.MILISEC_IN_HOUR
import com.kstorozh.evozhuk.TIME_ZONE
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
    }
}