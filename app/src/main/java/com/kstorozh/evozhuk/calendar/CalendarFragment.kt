package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.LOG_TAG
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.text.SimpleDateFormat

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

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val calendar1 = Calendar.getInstance()
        calendar1.setTime(sdf.parse("05-08-2019"))
        val calendar2 = Calendar.getInstance()
        calendar2.setTime(sdf.parse("16-08-2019"))
        /*val model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }*/

        val model = ViewModelProviders.of(this)[CalendarViewModel::class.java]
        handleErrors(model, view)

        observe(model.bookings(calendar1.timeInMillis, calendar2.timeInMillis)) {

            it.forEach { (key, value) ->
                Log.d(LOG_TAG, "$key $value")
            }
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
