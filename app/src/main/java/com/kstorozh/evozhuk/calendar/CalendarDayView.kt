package com.kstorozh.evozhuk.calendar

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kstorozh.evozhuk.R


class CalendarDayView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
