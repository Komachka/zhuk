package com.kstorozh.evozhuk.calendar

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.applandeo.materialcalendarview.EventDay
import com.kstorozh.evozhuk.R
import java.util.*
import java.text.SimpleDateFormat
import android.graphics.Color.parseColor
import android.graphics.drawable.Drawable
import com.kstorozh.evozhuk.utils.DrawableUtils


class CalendarViewModel : ViewModel() {

    fun getBookingDayInfo(): MutableLiveData<List<EventDay>>
    {

        var dt = "2019-08-01"  // Start date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = sdf.parse(dt)
        c.add(Calendar.DATE, 1)  // number of days to add
        dt = sdf.format(c.time)


        val day1 =  DayBooking(Calendar.getInstance(), isMyBookingExists = true, isAllDayBooked = true)
        val day2 = DayBooking(c, false, false)
        val dayBooking = listOf(day1, day2)

        val myBooking = R.mipmap.ic_info
        val allDay = R.drawable.sample_three_icons
        dayBooking.forEach { dayBooking->

        }

        val list =  mutableListOf(
            EventDay(day1.calendar, myBooking),
            EventDay(day2.calendar, R.drawable.sample_three_icons),
            EventDay(day2.calendar, R.drawable.alarm_clock))

        //list.add(EventDay(day1.calendar, R.mipmap.ic_hand))
        val liveData = MutableLiveData<List<EventDay>>()
        liveData.value = list
        return liveData

    }

}