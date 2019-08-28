package com.kstorozh.evozhuk.calendar_day

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.*

import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.toolbarDay
import org.joda.time.DateTime

class CalendarDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_parent_view, container, false)
    }

    private lateinit var onPageChangeListener: ViewPagerScrollListener
    private lateinit var adapter: DayPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
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

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(fragmentView.toolbarDay)
        toolbarDay.navigationIcon = resources.getDrawable(R.drawable.ic_close_black_24dp)
        toolbarDay.title = resources.getString(R.string.calendar)
        toolbarDay.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }
        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec
        adapter = DayPageAdapter(childFragmentManager, userId.toInt(), milisec)
        my_viewpager.adapter = adapter
        my_viewpager.currentItem = 1
        val cuurentTime = DateTime(milisec)
        onPageChangeListener =
            ViewPagerScrollListener(my_viewpager, adapter, cuurentTime)
        my_viewpager.addOnPageChangeListener(onPageChangeListener)
    }
}
