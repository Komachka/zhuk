package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.MONTH_DELTA
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import org.joda.time.DateTime

import java.util.*

class CalendarFragment : Fragment(), HandleErrors {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val today = Calendar.getInstance()
        val lastDay = Calendar.getInstance()
        lastDay.add(Calendar.MONTH, MONTH_DELTA)

        view.init(today, lastDay)
        val model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        viewLifecycleOwner.handleErrors(model, view)
        val userId = CalendarFragmentArgs.fromBundle(arguments!!).userId
        viewLifecycleOwner.observe(model.getBookingEvents(today.timeInMillis, lastDay.timeInMillis, userId.toInt())) {
            view.calendarView.setEvents(it)
        }
        view.calendarView.setOnDayClickListener { eventDay ->
            if (eventDay.calendar.get(Calendar.DAY_OF_MONTH) >= Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            Navigation.findNavController(view)
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToCalendarDayView(userId, eventDay.calendar.timeInMillis))
        }
    }

    private fun View.init(today: Calendar, lastDay: Calendar) {
        calendarView.setMaximumDate(lastDay)
        val calendars = ArrayList<Calendar>()
        val firstDayOfMonth = DateTime(today).withDayOfMonth(1)
        val fd = Calendar.getInstance()
        fd.timeInMillis = firstDayOfMonth.millis
        calendarView.setMinimumDate(fd)

        var i = firstDayOfMonth
        var dtoday = DateTime(System.currentTimeMillis())
        while (i.dayOfMonth < dtoday.dayOfMonth - 1) {
            i = i.plusDays(1)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = i.millis
            calendars.add(calendar)
        }
        calendarView.setDisabledDays(calendars)
    }
}
