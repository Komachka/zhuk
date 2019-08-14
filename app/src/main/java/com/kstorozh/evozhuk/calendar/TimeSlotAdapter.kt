package com.kstorozh.evozhuk.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.empty_time_slot.view.timeTv
import kotlinx.android.synthetic.main.busy_time_slot_with_login_item.view.*

class TimeSlotAdapter(val timeSlot: List<TimeSlot>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val EMPTY_VIEW_TYPE = 0
    private val BOOKING_VIEW_TYPE = 1
    private val CONTINUE_BOOKING_VIEW_TYPE = 2

    class EmptySlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            view.timeTv.text = timeSlot.timeLable
        }
    }

    class BusySlotWithLoginViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            if (timeSlot.isOtherBooking)
                view.userIndex.setBackgroundResource(R.color.other_booking_colour)
            view.timeTv.text = timeSlot.timeLable
            view.slackNameTv.text = timeSlot.booking!!.slackUserName
            view.timePeriodTv.text = "${timeSlot.slotStartDate}-${timeSlot.slotEndDate}"
        }
    }

    class BusySlotNoLoginViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            view.timeTv.text = timeSlot.timeLable
            if (timeSlot.isOtherBooking)
                view.userIndex.setBackgroundResource(R.color.other_booking_colour)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            EMPTY_VIEW_TYPE ->
            {
                view = LayoutInflater.from(parent.context).inflate(R.layout.empty_time_slot, parent, false)
                EmptySlotViewHolder(view)
            }
            BOOKING_VIEW_TYPE ->
            {
                view = LayoutInflater.from(parent.context).inflate(R.layout.busy_time_slot_with_login_item, parent, false)
                BusySlotWithLoginViewHolder(view)
            }
            else ->
            {
                view = LayoutInflater.from(parent.context).inflate(R.layout.busy_time_slot_no_login_item, parent, false)
                BusySlotNoLoginViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return timeSlot.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == BOOKING_VIEW_TYPE -> (viewHolder as BusySlotWithLoginViewHolder).setMyBookingView(timeSlot[position])
            getItemViewType(position) == EMPTY_VIEW_TYPE -> (viewHolder as EmptySlotViewHolder).setMyBookingView(timeSlot[position])
            else -> (viewHolder as BusySlotNoLoginViewHolder).setMyBookingView(timeSlot[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            (timeSlot[position].isMyBooking || timeSlot[position].isOtherBooking) && !timeSlot[position].isContinue -> BOOKING_VIEW_TYPE
            timeSlot[position].isContinue -> CONTINUE_BOOKING_VIEW_TYPE
            else -> EMPTY_VIEW_TYPE
        }
    }
}