package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar_day_view.*
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.dateTV
import java.text.SimpleDateFormat

const val USER_ID = "user_id"
const val MILISEC = "milisec"

class ChildrenDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var model: CalendarViewModel
    lateinit var fragmentView: View

    companion object {
        fun newInstance(milisec: Long, userId: Int): ChildrenDayFragment {
            val myFragment = ChildrenDayFragment()
            val args = Bundle()
            args.putLong(MILISEC, milisec)
            args.putInt(USER_ID, userId)
            myFragment.arguments = args
            return myFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {

        this.fragmentView = fragmentView
        model = activity!!.run { ViewModelProviders.of(this)[CalendarViewModel::class.java] }
        viewLifecycleOwner.handleErrors(model, fragmentView)
        viewManager = LinearLayoutManager(context)
        val milisec = arguments?.getLong(MILISEC) ?: 0
        val userId = arguments?.getInt(USER_ID) ?: 0
        updateUI(milisec, userId)
    }

    fun updateUI(milisec: Long, userId: Int) {

        if (dateTV == null) return
        dateTV.text = SimpleDateFormat(YEAR_MONTH_DAY_FORMAT).format(milisec)
        viewLifecycleOwner.observe(model.bookingSlotsPerDay(milisec, userId)) {
            viewAdapter = TimeSlotAdapter(it)
            fragmentView.recyclerView.adapter = viewAdapter
            fragmentView.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, fragmentView.recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val itemPosition = fragmentView.recyclerView.getChildLayoutPosition(view)
                        createDialog(it[itemPosition], userId.toString())
                    }
                    override fun onLongItemClick(view: View?, position: Int) {
                        // TODO update booking
                    }
                })
            )
        }
        fragmentView.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
    }
}
