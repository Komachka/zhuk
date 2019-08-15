package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import java.text.SimpleDateFormat


const val USER_ID = "user_id"
const val MILISEC = "milisec"

class ChildrenDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var model: CalendarViewModel


    companion object {
        fun newInstance(milisec: Long, userId:Int): ChildrenDayFragment {
            val myFragment = ChildrenDayFragment()

            val args = Bundle()
            args.putLong(MILISEC, milisec)
            args.putInt(USER_ID, userId)
            myFragment.arguments = args
            Log.d(LOG_TAG, "new instance ${userId} milisec ${milisec}")
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


        (activity as AppCompatActivity).setSupportActionBar(fragmentView.toolbarDay)
        fragmentView.toolbarDay.navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
        fragmentView.toolbarDay.title = resources.getString(R.string.calendar)
        fragmentView.toolbarDay.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }
        val userId = arguments?.getInt(USER_ID) ?: 0
        val milisec = arguments?.getLong(MILISEC) ?: 0
        model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        fragmentView.dateTV.text = SimpleDateFormat(YEAR_MONTH_DAY_FORMAT).format(milisec)
        viewLifecycleOwner.handleErrors(model, fragmentView)
        viewLifecycleOwner.observe(model.getBookingSlotsPerDay(milisec, userId)) {
            viewAdapter = TimeSlotAdapter(it)
            fragmentView.recyclerView.adapter = viewAdapter
            fragmentView.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, fragmentView.recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val itemPosition = fragmentView.recyclerView.getChildLayoutPosition(view)
                        userId?.let {userId ->
                            createDialog(it[itemPosition], userId.toString())
                        }
                    }
                    override fun onLongItemClick(view: View?, position: Int) {
                        // TODO update booking
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
