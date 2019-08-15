package com.kstorozh.evozhuk.calendar

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentStatePagerAdapter


import android.content.Context
import android.view.*

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter


class DayPageAdapter(manager: FragmentManager, private val viewPager: ViewPager) : FragmentStatePagerAdapter(manager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val setFragment = HashSet<Long>()


    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        val milisec = fragment.arguments?.getLong(MILISEC) ?: 0
        if (!setFragment.contains(milisec) && milisec != 0L) {
            mFragmentList.add(fragment)
            setFragment.add(milisec)
        }
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

