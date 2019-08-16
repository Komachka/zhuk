package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.R

import androidx.viewpager.widget.ViewPager
import com.kstorozh.evozhuk.LOG_TAG
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.*
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.*
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.my_viewpager
import org.joda.time.DateTime
import org.koin.androidx.scope.currentScope

class CalendarDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_parent_view, container, false)
    }




    private lateinit var onPageChangeListener: MyListener

    private var lastPosition = 0
    private lateinit var adapter: DayPageAdapter
    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {

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
        var cuurentTime = DateTime(milisec)
        lastPosition = 1

        onPageChangeListener = MyListener(fragmentView.my_viewpager, adapter)
        onPageChangeListener.setCurrentDate(cuurentTime)
    }



}



class MyListener(val my_viewpager:ViewPager, val adapter: DayPageAdapter): ViewPager.OnPageChangeListener
{
    var selectedPos = 1
    var blockRight = false
    var blockLeft = false
    val midlePos = 1
    val maxTime = DateTime().plusDays(60)
    val minTime = DateTime()
    lateinit var cuurentTime:DateTime

    fun setCurrentDate(dateTime: DateTime) {
        cuurentTime = dateTime
        if (dateTime == minTime) {
            blockLeft = true
            my_viewpager.currentItem = 0
            selectedPos = 0
        } else if (dateTime == maxTime) {
            blockRight = true
            my_viewpager.currentItem = 2
            selectedPos = 2
        } else {
            blockLeft = false
            blockRight = false
        }
        adapter.setCurrentDate(dateTime)
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            var date: DateTime? = null
            if (selectedPos < midlePos) {
                if (blockLeft) return
                blockRight = false
                date = DateTime(cuurentTime).plusDays(-1)
                cuurentTime = date
            } else if (selectedPos > midlePos) {
                if (blockRight) return
                blockLeft = false
                date = DateTime(cuurentTime).plusDays(1)
                cuurentTime = date
            }
            if (date != null && date == maxTime) {
                blockRight = true
            } else if (date != null && date == minTime) {
                blockLeft = true
            } else {
                my_viewpager.currentItem = 1
            }
        }

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // Log.e(LOG_TAG, "$position , $positionOffset , $positionOffsetPixels")
    }

    override fun onPageSelected(position: Int) {

        selectedPos = when {
            blockRight -> 0
            blockLeft -> 2
            else -> position
        }
    }

}