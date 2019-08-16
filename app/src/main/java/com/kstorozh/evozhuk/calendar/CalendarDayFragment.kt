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
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.*
import org.joda.time.DateTime


class CalendarDayFragment : Fragment(), BottomSheetDialogHandler, HandleErrors {




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_parent_view, container, false)
    }

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


        adapter = DayPageAdapter(fragmentManager!!, fragmentView.my_viewpager, userId.toInt())



        val dateTime = DateTime(milisec)
        adapter.addFragment(ChildrenDayFragment.newInstance(dateTime.plusDays(-1).millis, userId.toInt()))
        adapter.addFragment(ChildrenDayFragment.newInstance(milisec, userId.toInt()))
        adapter.addFragment(ChildrenDayFragment.newInstance(dateTime.plusDays(1).millis, userId.toInt()))

        fragmentView.my_viewpager.adapter = adapter
        fragmentView.my_viewpager.currentItem = 1
        var cuurentTime = DateTime(milisec)
        lastPosition = 1



        fragmentView.my_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //Log.e(LOG_TAG, "$position , $positionOffset , $positionOffsetPixels")

            }

            override fun onPageSelected(position: Int) {

                Log.d(LOG_TAG, "cuurentTime ${cuurentTime}")
                if (position > 1) {
                    Log.d(LOG_TAG, "<-")
                    cuurentTime = DateTime(cuurentTime).plusDays(1)
                    adapter.updateTime(cuurentTime)
                    fragmentView.my_viewpager.currentItem = 1
                    lastPosition = position

                } else if (position < 1) {
                    Log.d(LOG_TAG, "->")
                    cuurentTime = DateTime(cuurentTime).plusDays(-1)
                    adapter.updateTime(cuurentTime)
                    fragmentView.my_viewpager.currentItem = 1
                    lastPosition = position

                }


            }

        })
    }





}
