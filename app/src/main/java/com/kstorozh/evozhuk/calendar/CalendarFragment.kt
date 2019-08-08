package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar.view.*

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

        val calendar1 = Calendar.getInstance()
        calendar1.add(Calendar.DAY_OF_MONTH, 1)
        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DAY_OF_MONTH, 31)
        val model = activity!!.run {
            CalendarViewModelFactory(calendar1.timeInMillis, calendar2.timeInMillis).create(CalendarViewModel::class.java)
        }
        observe(model.getBookingDaysInfo()) {
            view.calendarView.setEvents(it)
        }
        view.calendarView.setOnDayClickListener { eventDay ->
            val userId = CalendarFragmentArgs.fromBundle(arguments!!).userId
            Navigation.findNavController(view)
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToCalendarDayView(userId, eventDay.calendar.timeInMillis))
        }
    }
}
