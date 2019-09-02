package com.kstorozh.evozhuk.calendar_day

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_calendar_day_view.*
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.*
import org.joda.time.DateTime
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
            recyclerView.adapter = TimeSlotAdapter()
        }
        viewLifecycleOwner.observe(model.durationInMilisecLiveData) {
            viewLifecycleOwner.observe(model.bookingSlotsPerDay) {
                (fragmentView.recyclerView.adapter as TimeSlotAdapter).updateData(it)
                (recyclerView.adapter as TimeSlotAdapter).createDialogListener = { view ->
                    val itemPosition = fragmentView.recyclerView.getChildLayoutPosition(view)
                    createBookingDialog(it[itemPosition], userId.toString())
                }
                (recyclerView.adapter as TimeSlotAdapter).editBookingListener = { pos ->
                    editBookingDialog(it[pos], userId.toString())
                }
                (recyclerView.adapter as TimeSlotAdapter).deleteBookingListener = { pos ->
                    deleteBookingDialog(it[pos]) {
                        viewLifecycleOwner.observe(model.deleteBooking(userId.toString(), it,
                            DateTime().millis, DateTime().plusMonths(2).millis)) {
                            if (it)
                                view?.showSnackbar(resources.getString(R.string.bookin_removed))
                        }
                    }
                }
            }
        }
        updateUI(milisec, userId)
    }

    override fun onResume() {
        super.onResume()
        val milisec = arguments?.getLong(MILISEC) ?: 0
        val userId = arguments?.getInt(USER_ID) ?: 0
        updateUI(milisec, userId)
    }

    fun updateUI(milisec: Long, userId: Int) {
        val args = Bundle()
        args.putLong(MILISEC, milisec)
        args.putInt(USER_ID, userId)
        args.putInt(FRAGMENT_ID, id)
        arguments = args
        if (dateTV == null) return
        model.getBookingInfo(milisec, userId)
        dateTV.text = SimpleDateFormat(DAY_MONTH_FORMAT).format(milisec)
    }
}
