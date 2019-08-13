package com.kstorozh.evozhuk.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.empty_time_slot.view.timeTv
import kotlinx.android.synthetic.main.time_slot_item.view.*

class TimeSlotAdapter(val timeSlot: List<TimeSlot>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val EMPTY_VIEW_TYPE = 0
    private val MY_BOOKING_VIEW_TYPE = 1
    private val OTHER_BOOKING_VIEW_TYPE = 2

    class EmptySlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            view.timeTv.text = timeSlot.slotStartDate
        }
    }

    class MySlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            view.timeTv.text = timeSlot.slotStartDate
            view.slackNameTv.text = timeSlot.booking!!.slackUserName
            view.timePeriodTv.text = "${timeSlot.slotStartDate}-${timeSlot.slotEndDate}"
        }
    }

    class AnotherPersonSlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            view.userIndex.setBackgroundResource(R.color.other_booking_colour)
            view.timeTv.text = timeSlot.slotStartDate
            view.slackNameTv.text = timeSlot.booking!!.slackUserName
            view.timePeriodTv.text = "${timeSlot.slotStartDate}-${timeSlot.slotEndDate}"
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
            MY_BOOKING_VIEW_TYPE ->
            {
                view = LayoutInflater.from(parent.context).inflate(R.layout.time_slot_item, parent, false)
                MySlotViewHolder(view)
            }
            else ->
            {
                view = LayoutInflater.from(parent.context).inflate(R.layout.time_slot_item, parent, false)
                AnotherPersonSlotViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return timeSlot.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == MY_BOOKING_VIEW_TYPE -> (viewHolder as MySlotViewHolder).setMyBookingView(timeSlot[position])
            getItemViewType(position) == EMPTY_VIEW_TYPE -> (viewHolder as EmptySlotViewHolder).setMyBookingView(timeSlot[position])
            else -> (viewHolder as AnotherPersonSlotViewHolder).setMyBookingView(timeSlot[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            timeSlot[position].isMyBooking -> MY_BOOKING_VIEW_TYPE
            timeSlot[position].isOtherBooking -> OTHER_BOOKING_VIEW_TYPE
            else -> EMPTY_VIEW_TYPE
        }
    }
}