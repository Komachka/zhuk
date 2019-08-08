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

        val events = ArrayList<EventDay>()

        val calendar = Calendar.getInstance()
        events.add(EventDay(calendar, R.drawable.sample_three_icons))

        val calendar1 = Calendar.getInstance()
        calendar1.add(Calendar.DAY_OF_MONTH, 2)
        events.add(EventDay(calendar1, R.drawable.sample_icon_2))

        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DAY_OF_MONTH, 5)
        events.add(EventDay(calendar2, R.drawable.sample_icon_3))

        val calendar3 = Calendar.getInstance()
        calendar3.add(Calendar.DAY_OF_MONTH, 7)
        events.add(EventDay(calendar3, R.drawable.sample_four_icons))

        val calendar4 = Calendar.getInstance()
        calendar4.add(Calendar.DAY_OF_MONTH, 13)
        events.add(EventDay(calendar4, R.drawable.sample_three_icons))

        val liveData = MutableLiveData<List<EventDay>>()
        liveData.value = events
        return liveData

    }

}