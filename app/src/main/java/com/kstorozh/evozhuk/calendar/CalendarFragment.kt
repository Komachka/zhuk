package com.kstorozh.evozhuk.calendar

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException
import com.applandeo.materialcalendarview.utils.DateUtils
import com.kstorozh.evozhuk.DATE_FORMAT_DAY
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.home.HomeViewModel
import com.kstorozh.evozhuk.utils.DrawableUtils
import com.kstorozh.evozhuk.utils.observe

import kotlinx.android.synthetic.main.fragment_calendar.view.*
import com.kstorozh.evozhuk.utils.showSnackbar
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val events = ArrayList<EventDay>()

        val calendar = Calendar.getInstance()
        events.add(EventDay(calendar, DrawableUtils.getCircleDrawableWithText(context, "M")))

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
        events.add(EventDay(calendar4, DrawableUtils.getThreeDots(context)))

        val calendarView = view.findViewById<View>(R.id.calendarView) as CalendarView

        val min = Calendar.getInstance()
        min.add(Calendar.MONTH, -2)

        val max = Calendar.getInstance()
        max.add(Calendar.MONTH, 2)

        calendarView.setMinimumDate(min)
        calendarView.setMaximumDate(max)

        calendarView.setEvents(events)

        calendarView.setDisabledDays(getDisabledDays())

        calendarView.setOnDayClickListener { eventDay ->
            Toast.makeText(
                context,
                eventDay.calendar.time.toString() + " "
                        + eventDay.isEnabled,
                Toast.LENGTH_SHORT
            ).show()
        }


    }


    private fun getDisabledDays(): List<Calendar> {
        val firstDisabled = DateUtils.getCalendar()
        firstDisabled.add(Calendar.DAY_OF_MONTH, 2)

        val secondDisabled = DateUtils.getCalendar()
        secondDisabled.add(Calendar.DAY_OF_MONTH, 1)

        val thirdDisabled = DateUtils.getCalendar()
        thirdDisabled.add(Calendar.DAY_OF_MONTH, 18)

        val calendars = ArrayList<Calendar>()
        calendars.add(firstDisabled)
        calendars.add(secondDisabled)
        calendars.add(thirdDisabled)
        return calendars
    }

    private fun getRandomCalendar(): Calendar {
        val random = Random()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, random.nextInt(99))

        return calendar
    }
}
