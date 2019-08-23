package com.kstorozh.evozhuk.calendar_day

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
import java.text.SimpleDateFormat

const val USER_ID = "user_id"
const val MILISEC = "milisec"
const val FRAGMENT_ID = "id"

class ChildrenDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var model: DayViewModel

    companion object {
        fun newInstance(milisec: Long, userId: Int, id: Int): ChildrenDayFragment {
            val myFragment = ChildrenDayFragment()
            val args = Bundle()
            args.putLong(MILISEC, milisec)
            args.putInt(USER_ID, userId)
            args.putInt(FRAGMENT_ID, id)
            myFragment.arguments = args
            return myFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.getInt(FRAGMENT_ID) ?: -1
        model = ViewModelProviders.of(this)[DayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.handleErrors(model, fragmentView)
        viewManager = LinearLayoutManager(context)
        val milisec = arguments?.getLong(MILISEC) ?: 0
        val userId = arguments?.getInt(USER_ID) ?: 0
        fragmentView.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            viewAdapter = TimeSlotAdapter()
            fragmentView.recyclerView.adapter = viewAdapter
        }
        viewLifecycleOwner.observe(model.bookingSlotsPerDay) {
            (viewAdapter as TimeSlotAdapter).updateData(it)
            viewAdapter.notifyDataSetChanged()
            fragmentView.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(
                    context!!,
                    fragmentView.recyclerView,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            val itemPosition = fragmentView.recyclerView.getChildLayoutPosition(view)
                            if (!it[itemPosition].isMyBooking && !it[itemPosition].isOtherBooking)
                                createDialog(it[itemPosition], userId.toString())
                        }
                        override fun onLongItemClick(view: View?, position: Int) {
                            // TODO update booking
                        }
                    })
            )
        }
        updateUI(milisec, userId)
    }

    fun updateUI(milisec: Long, userId: Int) {
        if (dateTV == null) return
        model.getBookingInfo(milisec, userId)
        dateTV.text = SimpleDateFormat(DAY_MONTH_FORMAT).format(milisec)
    }
}
