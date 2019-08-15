package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*

class CalendarDayFragment : Fragment(), BottomSheetDialogHandler {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var model: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec
        model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        viewLifecycleOwner.observe(model.getBookingSlotsPerDay(milisec, userId.toInt())) {
            viewAdapter = TimeSlotAdapter(it)
            fragmentView.recyclerView.adapter = viewAdapter
            fragmentView.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, fragmentView.recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val itemPosition = fragmentView.recyclerView.getChildLayoutPosition(view)
                        createDialog(it[itemPosition], userId)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // do whatever
                    }
                })
            )
        }
        viewManager = LinearLayoutManager(context)
        fragmentView.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
    }
}
