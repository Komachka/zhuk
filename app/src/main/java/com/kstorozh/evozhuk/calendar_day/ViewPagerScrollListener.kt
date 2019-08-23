package com.kstorozh.evozhuk.calendar_day

import androidx.viewpager.widget.ViewPager
import com.kstorozh.evozhuk.MONTH_DELTA
import org.joda.time.DateTime

class ViewPagerScrollListener(
    val viewPager: ViewPager,
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

    val maxTime = DateTime().plusMonths(MONTH_DELTA)
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
                viewPager.setCurrentItem(MIDDLE_POS, false)
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
    override fun onPageSelected(position: Int) {
        selectedPos = when {
            blockLeft -> RIGHT_POS
            blockRight -> LEFT_POS
            else -> position
        }
    }
}
