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
import com.kstorozh.evozhuk.HandleErrors
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_calendar_day_view.view.*
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_info.view.*

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kstorozh.evozhuk.LOG_TAG
import kotlinx.android.synthetic.main.fragment_calendar_parent_view.view.*
import org.joda.time.DateTime
import android.widget.LinearLayout








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

        adapter = DayPageAdapter(fragmentManager!!, fragmentView.my_viewpager)

        val userId = CalendarDayFragmentArgs.fromBundle(arguments!!).userId
        val milisec = CalendarDayFragmentArgs.fromBundle(arguments!!).milisec

        val dateTime = DateTime(milisec)
        adapter.addFragment(ChildrenDayFragment.newInstance(dateTime.plusDays(-1).millis, userId.toInt()))
        adapter.addFragment(ChildrenDayFragment.newInstance(milisec, userId.toInt()))
        adapter.addFragment(ChildrenDayFragment.newInstance(dateTime.plusDays(1).millis, userId.toInt()))

        fragmentView.my_viewpager.adapter = adapter
        fragmentView.my_viewpager.currentItem = 1
        lastPosition = adapter.count - 2


        fragmentView.my_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //Log.e(LOG_TAG, "$position , $positionOffset , $positionOffsetPixels")

            }

            override fun onPageSelected(position: Int) {
                if (lastPosition > position) {


                    Log.d(LOG_TAG, " LEFT")

                    // TODO remove last fragment
                    // TODO add fragment -1
                }else if (lastPosition < position) {
                    Log.d(LOG_TAG, " RIGHT")

                    val fragment = adapter.getItem(position)
                    val userId = fragment.arguments?.getInt(USER_ID) ?: 0
                    val milisec = fragment.arguments?.getLong(MILISEC) ?: 0

                    val maxTime = DateTime(System.currentTimeMillis()).plusDays(10)
                    //val maxTime = DateTime(milisec).plusMonths(2)
                    val dateTime = DateTime(milisec)

                    Log.d(LOG_TAG, "${dateTime} ${maxTime}")
                    if (dateTime.millis < maxTime.millis)
                        addRight(ChildrenDayFragment.newInstance(dateTime.plusDays(1).millis, userId))


                    // TODO remove 0 fragment
                    // TODO add fragment +1
                }
                lastPosition = position
                Log.d(LOG_TAG, " position $lastPosition")
            }

        })
    }

    private fun addRight(fragment: Fragment) {
        adapter.addFragment(fragment)
        adapter.notifyDataSetChanged()
    }

    private fun remove(position:Int) {
        adapter.removeFrag(position)
        adapter.notifyDataSetChanged()
    }



}
