package com.evo.evozhuk.calendar_day

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.joda.time.DateTime

class DayPageAdapter(manager: FragmentManager, val userId: Int, val milisec: Long) :
    FragmentPagerAdapter(manager) {

    companion object {
        const val size = 3
    }

    private val fragments: List<ChildrenDayFragment>

    init {
        val dateTime = DateTime(milisec)

        fragments = listOf(
            ChildrenDayFragment.newInstance(
                dateTime.plusDays(-1).millis,
                userId,
                0
            ),
            ChildrenDayFragment.newInstance(milisec, userId, 1),
            ChildrenDayFragment.newInstance(
                dateTime.plusDays(1).millis,
                userId,
                2
            )
        )
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return size
    }

    fun setCurrentDate(date: DateTime) {
        fragments[1].updateUI(date.millis, userId)
        fragments[0].updateUI(date.plusDays(-1).millis, userId)
        fragments[2].updateUI(date.plusDays(1).millis, userId)
    }
}
