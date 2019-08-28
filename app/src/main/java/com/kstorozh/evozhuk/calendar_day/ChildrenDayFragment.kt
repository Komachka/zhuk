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

class ChildrenDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

    lateinit var model: DayViewModel

    companion object {
        private const val USER_ID = "user_id"
        private const val MILISEC = "milisec"
        private const val FRAGMENT_ID = "id"

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_calendar_day_view, container, false)
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        model = ViewModelProviders.of(this)[DayViewModel::class.java]
        viewLifecycleOwner.handleErrors(model, fragmentView)
        val milisec = arguments?.getLong(MILISEC) ?: 0
        val userId = arguments?.getInt(USER_ID) ?: 0

        fragmentView.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            fragmentView.recyclerView.adapter = TimeSlotAdapter()
        }
        viewLifecycleOwner.observe(model.durationInMilisecLiveData) {
            viewLifecycleOwner.observe(model.bookingSlotsPerDay) {
                (fragmentView.recyclerView.adapter as TimeSlotAdapter).updateData(it)
                fragmentView.recyclerView.addOnItemTouchListener(
                    RecyclerItemClickListener(
                        context!!,
                        fragmentView.recyclerView,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (it.isEmpty()) return
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
        }
        updateUI(milisec, userId)
    }

    fun updateUI(milisec: Long, userId: Int) {
        if (dateTV == null) return
        model.getBookingInfo(milisec, userId)
        dateTV.text = SimpleDateFormat(DAY_MONTH_FORMAT).format(milisec)
    }
}
