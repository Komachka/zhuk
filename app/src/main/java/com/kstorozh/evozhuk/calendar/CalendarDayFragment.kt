package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_info.view.*

class CalendarDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(fragmentView.toolbarDay)
        fragmentView.toolbarDay.navigationIcon = resources.getDrawable(R.drawable.ic_close_black_24dp)
        fragmentView.toolbarDay.title = resources.getString(R.string.calendar)
        fragmentView.toolbarDay.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }
        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec
        model = activity!!.run {
            ViewModelProviders.of(this)[CalendarViewModel::class.java]
        }
        viewLifecycleOwner.handleErrors(model, fragmentView)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.action_info) {
                // TODO add navigation
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
