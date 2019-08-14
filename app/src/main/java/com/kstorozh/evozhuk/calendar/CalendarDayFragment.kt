package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.DATE_FORMAT_NOTIFICATION_MESSAGE

import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarDayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec
        val model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        observe(model.getBookingSlotsPerDay(milisec, userId.toInt())) {
            viewAdapter = TimeSlotAdapter(it)
            view.recyclerView.adapter = viewAdapter
        }
        viewManager = LinearLayoutManager(context)
        view.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
    }
}
