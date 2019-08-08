package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.utils.DateUtils
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe

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

        val calendarView = view.findViewById<View>(R.id.calendarView) as CalendarView

        val calendar1 = Calendar.getInstance()
        calendar1.add(Calendar.DAY_OF_MONTH, 1)

        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DAY_OF_MONTH, 31)

        val model = activity!!.run {
            CalendarViewModelFactory(calendar1.timeInMillis, calendar2.timeInMillis).create(CalendarViewModel::class.java)
        }

        val min = Calendar.getInstance()
        min.add(Calendar.MONTH, -2)

        val max = Calendar.getInstance()
        max.add(Calendar.MONTH, 2)

        calendarView.setMinimumDate(min)
        calendarView.setMaximumDate(max)

        observe(model.getBookingDaysInfo()) {
            calendarView.setEvents(it)
            calendarView.setDisabledDays(getDisabledDays())
        }

        calendarView.setOnDayClickListener { eventDay ->
            Toast.makeText(
                context,
                eventDay.calendar.time.toString() + " " +
                        eventDay.isEnabled,
                Toast.LENGTH_SHORT
            ).show()
            val userId = CalendarFragmentArgs.fromBundle(arguments!!).userId
            Navigation.findNavController(calendarView)
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToCalendarDayView(userId, eventDay.calendar.timeInMillis))
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
}
