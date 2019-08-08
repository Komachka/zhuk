package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstorozh.evozhuk.DATE_FORMAT_NOTIFICATION_MESSAGE

import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.showSnackbar
import java.text.SimpleDateFormat

class CalendarDayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*val model = activity?.run {
            ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        }*/ // java.lang.RuntimeException: Cannot create an instance of class com.kstorozh.evozhuk.calendar.CalendarViewModel

        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec
        view.showSnackbar("userId $userId " +
                "date ${SimpleDateFormat(DATE_FORMAT_NOTIFICATION_MESSAGE).format(milisec)}")
    }
}
