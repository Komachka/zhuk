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
import com.kstorozh.evozhuk.DATE_FORMAT_NOTIFICATION_MESSAGE
import com.kstorozh.evozhuk.utils.showSnackbar
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.calendarView.setOnDayClickListener { eventDay ->
            view.showSnackbar(
                SimpleDateFormat(DATE_FORMAT_NOTIFICATION_MESSAGE).format(eventDay.calendar.timeInMillis)
            )
        }
    }


}
