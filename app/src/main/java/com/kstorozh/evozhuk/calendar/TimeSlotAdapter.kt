package com.kstorozh.evozhuk.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.empty_time_slot.view.timeTv
import kotlinx.android.synthetic.main.time_slot_item.view.*

class TimeSlotAdapter(val booking: List<Booking>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val EMPTY_VIEW_TYPE = 0
    private val MY_BOOKING_VIEW_TYPE = 1
    private val OTHER_BOOKING_VIEW_TYPE = 2

    class EmptySlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(booking: Booking) {
            view.timeTv.text = booking.calendar.time.toString()
        }
    }

    class MySlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(booking: Booking) {
            view.timeTv.text = booking.calendar.time.toString()
            view.slackNameTv.text = booking.slackName
            view.timePeriodTv.text = "${booking.startDate}-${booking.endDate}"
        }
    }

    class AnotherPersonSlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(booking: Booking) {
            view.timeTv.text = booking.calendar.time.toString()
            view.slackNameTv.text = booking.slackName
            view.timePeriodTv.text = "${booking.startDate}-${booking.endDate}"
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
        return booking.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == MY_BOOKING_VIEW_TYPE -> (viewHolder as MySlotViewHolder).setMyBookingView(booking[position])
            getItemViewType(position) == EMPTY_VIEW_TYPE -> (viewHolder as EmptySlotViewHolder).setMyBookingView(booking[position])
            else -> (viewHolder as AnotherPersonSlotViewHolder).setMyBookingView(booking[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            booking[position].isMyBooking -> MY_BOOKING_VIEW_TYPE
            booking[position].isOtherBooking -> OTHER_BOOKING_VIEW_TYPE
            else -> EMPTY_VIEW_TYPE
        }
    }
}