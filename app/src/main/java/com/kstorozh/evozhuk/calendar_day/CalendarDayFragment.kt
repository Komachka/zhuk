package com.kstorozh.evozhuk.calendar_day

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.R

import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.*
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.my_viewpager
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
    private lateinit var fragment: View
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
        fragment = fragmentView
        (activity as AppCompatActivity).setSupportActionBar(fragmentView.toolbarDay)
        fragmentView.toolbarDay.navigationIcon = resources.getDrawable(R.drawable.ic_close_black_24dp)
        fragmentView.toolbarDay.title = resources.getString(R.string.calendar)
        fragmentView.toolbarDay.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }
        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec
        adapter = DayPageAdapter(childFragmentManager, userId.toInt(), milisec)
        fragmentView.my_viewpager.adapter = adapter
        fragmentView.my_viewpager.currentItem = 1

        val cuurentTime = DateTime(milisec)
        onPageChangeListener =
            ViewPagerScrollListener(fragmentView.my_viewpager, adapter, cuurentTime)
        fragmentView.my_viewpager.addOnPageChangeListener(onPageChangeListener)
    }
}
