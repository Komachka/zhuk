package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstorozh.evozhuk.DATE_FORMAT_DAY
import com.kstorozh.evozhuk.R

import kotlinx.android.synthetic.main.fragment_calendar.view.*
import com.kstorozh.evozhuk.DATE_FORMAT_NOTIFICATION_MESSAGE
import com.kstorozh.evozhuk.utils.showSnackbar
import java.text.SimpleDateFormat

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
                SimpleDateFormat(DATE_FORMAT_DAY).format(eventDay.calendar.timeInMillis)
            )
        }
    }
}
