package com.kstorozh.evozhuk.chooseTime

import java.util.*

class TimeUtils {
    companion object {

        val dateFormat = "HH:mm dd MMMM"
        fun getCurrentTimeZone(): TimeZone {
            return TimeZone.getTimeZone("Europe/Kiev")
        }

        fun setHours(hour: Int): Long {
            return System.currentTimeMillis() + 3600000L * hour
        }
    }
}