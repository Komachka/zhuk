package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstorozh.evozhuk.R

import com.applandeo.materialcalendarview.EventDay
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import android.widget.Toast

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.datePicker.setOnDayClickListener { eventDay -> doSmth(eventDay) }
    }

    private fun doSmth(eventDay: EventDay) {
        Toast.makeText(context, eventDay.calendar.toString() + " " + eventDay.toString(), Toast.LENGTH_LONG).show()
    }
}
