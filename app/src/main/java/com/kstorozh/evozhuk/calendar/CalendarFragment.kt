package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.MONTH_DELTA
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.*

import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel

import java.util.*

class CalendarFragment : Fragment(), HandleErrors {

    val model: CalendarViewModel by viewModel()

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
            setTitleTextColor(resources.getColor(R.color.logoTextColour))
            setNavigationOnClickListener {
                val navController = this.findNavController()
                navController.navigateUp()
            }
        }
        val today = Calendar.getInstance()
        val lastDay = Calendar.getInstance()
        lastDay.add(Calendar.MONTH, MONTH_DELTA)

        view.init(today, lastDay)
        viewLifecycleOwner.handleErrors(model, view)
        val userId = CalendarFragmentArgs.fromBundle(arguments!!).userId
        viewLifecycleOwner.observe(model.getBookingEvents(today.timeInMillis, lastDay.timeInMillis, userId.toInt())) {
            view.calendarView.setEvents(it)
        }
        view.calendarView.setOnDayClickListener { eventDay ->
            if (eventDay.calendar.timeInMillis >= DateTime().withTime(0, 0, 0, 0).millis)
            Navigation.findNavController(view)
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToCalendarDayView(userId, eventDay.calendar.timeInMillis))
        }
    }

    private fun View.init(today: Calendar, lastDay: Calendar) {
        calendarView.setMaximumDate(lastDay)
        val disabledDays = ArrayList<Calendar>()
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
            disabledDays.add(calendar)
        }
        calendarView.setDisabledDays(disabledDays)
    }
}
