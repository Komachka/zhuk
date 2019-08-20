package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.MONTH_DELTA
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*

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

        (activity as AppCompatActivity).setSupportActionBar(view.toolbar_calendar)
        view.toolbar_calendar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_close_black_24dp)
            title = resources.getString(R.string.calendar)
            setNavigationOnClickListener {
                val navController = this.findNavController()
                navController.navigateUp()
            }
        }
        val today = Calendar.getInstance()
        val lastDay = Calendar.getInstance()
        lastDay.add(Calendar.MONTH, MONTH_DELTA)
        view.calendarView.setMaximumDate(lastDay)
        val model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        viewLifecycleOwner.handleErrors(model, view)
        val userId = CalendarFragmentArgs.fromBundle(arguments!!).userId
        viewLifecycleOwner.observe(model.getBookingEvents(today.timeInMillis, lastDay.timeInMillis, userId.toInt())) {
            view.calendarView.setEvents(it)
        }
        view.calendarView.setOnDayClickListener { eventDay ->
            Navigation.findNavController(view)
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToCalendarDayView(userId, eventDay.calendar.timeInMillis))
        }
    }
}
