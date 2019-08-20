package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.R

import androidx.viewpager.widget.ViewPager
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

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {

        fragment = fragmentView
        (activity as AppCompatActivity).setSupportActionBar(fragmentView.toolbarDay)
        fragmentView.toolbarDay.navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
        fragmentView.toolbarDay.title = resources.getString(R.string.calendar)
        fragmentView.toolbarDay.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }

        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec

        adapter = DayPageAdapter(fragmentManager!!, userId.toInt(), milisec)

        fragmentView.my_viewpager.adapter = adapter
        fragmentView.my_viewpager.currentItem = 1
        val cuurentTime = DateTime(milisec)
        onPageChangeListener = ViewPagerScrollListener(fragmentView.my_viewpager, adapter, cuurentTime)
        fragmentView.my_viewpager.addOnPageChangeListener(onPageChangeListener)
    }
}

class ViewPagerScrollListener(
    val my_viewpager: ViewPager,
    val adapter: DayPageAdapter,
    var cuurentTime: DateTime
) : ViewPager.OnPageChangeListener {

    companion object {
        const val MIDDLE_POS = 1
        const val RIGHT_POS = 2
        const val LEFT_POS = 0
    }

    var selectedPos = 1
    var blockRight = false
    var blockLeft = false

    val maxTime = DateTime().plusDays(60)
    val minTime = DateTime()

    override fun onPageScrollStateChanged(state: Int) {

        if (state == ViewPager.SCROLL_STATE_IDLE) {
            var date: DateTime? = null
            if (selectedPos < MIDDLE_POS) {
                if (blockLeft) return
                blockRight = false
                date = DateTime(cuurentTime).plusDays(-1)
                cuurentTime = date
                if (date.dayOfMonth != minTime.dayOfMonth)
                    adapter.setCurrentDate(cuurentTime)
            } else if (selectedPos > MIDDLE_POS) {
                if (blockRight) return
                blockLeft = false
                date = DateTime(cuurentTime).plusDays(1)
                cuurentTime = date
                if (date.dayOfMonth != maxTime.dayOfMonth)
                    adapter.setCurrentDate(cuurentTime)
            }
            if (date != null && date.dayOfMonth == maxTime.dayOfMonth && date.monthOfYear == maxTime.monthOfYear) {
                blockRight = true
            } else if (date != null && (date.dayOfMonth == minTime.dayOfMonth && date.monthOfYear == minTime.monthOfYear)) {
                blockLeft = true
            } else {
                my_viewpager.setCurrentItem(MIDDLE_POS,false)
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        selectedPos = when {
            blockLeft -> RIGHT_POS
            blockRight -> LEFT_POS
            else -> position
        }
    }
}