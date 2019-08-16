package com.kstorozh.evozhuk.calendar

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentStatePagerAdapter


import android.content.Context
import android.util.Log
import android.view.*

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.kstorozh.evozhuk.LOG_TAG
import com.kstorozh.evozhuk.R
import org.joda.time.DateTime


class DayPageAdapter(val manager: FragmentManager, private val viewPager: ViewPager, val userId:Int) : FragmentStatePagerAdapter(manager) {

    private val mFragmentList = ArrayList<ChildrenDayFragment>()
    private val setFragment = HashSet<Long>()
    val size = 3



    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return size
    }

    fun addFragment(fragment: ChildrenDayFragment) {
        val milisec = fragment.arguments?.getLong(MILISEC) ?: 0
        if (!setFragment.contains(milisec) && milisec != 0L) {
            mFragmentList.add(fragment)
            setFragment.add(milisec)
        }
    }




    fun updateTime(currentDateTime: DateTime)
    {
        Log.d(LOG_TAG, "update--------")
        Log.d(LOG_TAG, "yestoday ${currentDateTime.plusDays(-1)}")
        mFragmentList[0].updateUI(currentDateTime.plusDays(-1).millis, userId)


        val t = manager.beginTransaction()
        t.detach(mFragmentList[0])
        t.attach(mFragmentList[0])





        Log.d(LOG_TAG, "today ${currentDateTime}")
        mFragmentList[1].updateUI(currentDateTime.millis, userId)

        t.detach(mFragmentList[1])
        t.attach(mFragmentList[1])



        Log.d(LOG_TAG, "tomorow ${currentDateTime.plusDays(1)}")
        mFragmentList[2].updateUI(currentDateTime.plusDays(1).millis, userId)

        t.detach(mFragmentList[2])
        t.attach(mFragmentList[2])

        t.commit()

        Log.d(LOG_TAG, "--------update")
    }






    fun removeFrag(position: Int) {

        val fragment = mFragmentList.get(position)
        mFragmentList.remove(fragment)
        destroyFragmentView(viewPager, position, fragment)
        notifyDataSetChanged()
    }


    fun destroyFragmentView(container: ViewGroup, position: Int, `object`: Any) {
        val manager = (`object` as Fragment).getFragmentManager()
        val trans = manager?.beginTransaction()
        trans?.remove(`object` as Fragment)
        trans?.commit()
    }


    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}

