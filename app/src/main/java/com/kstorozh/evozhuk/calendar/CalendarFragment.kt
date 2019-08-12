package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar.view.*

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
        lastDay.add(Calendar.MONTH, 2)
        view.calendarView.setMaximumDate(lastDay)
        val model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        handleErrors(model, view)
        val userId = CalendarFragmentArgs.fromBundle(arguments!!).userId
        observe(model.getBookingEvents(today.timeInMillis, lastDay.timeInMillis, userId.toInt())) {
            view.calendarView.setEvents(it)
        }
        view.calendarView.setOnDayClickListener { eventDay ->
            Navigation.findNavController(view)
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToCalendarDayView(userId, eventDay.calendar.timeInMillis))
        }
    }
}
