package com.kstorozh.evozhuk.calendar_day

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.FIRST_HOUR
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.empty_time_slot.view.timeTv
import kotlinx.android.synthetic.main.busy_time_slot_with_login_item.view.*
import org.joda.time.DateTime

class TimeSlotAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val timeSlot = mutableListOf<TimeSlot>()
    inner class SlotsDiffCallback(private val oldList: List<TimeSlot>, private val newList: List<TimeSlot>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].timeLable == newList[newItemPosition].timeLable
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].range == newList[newItemPosition].range &&
                    oldList[oldItemPosition].booking == newList[newItemPosition].booking
        }
    }

    fun updateData(slots: List<TimeSlot>) {
        val calback = SlotsDiffCallback(timeSlot, slots)
        val diffRes = DiffUtil.calculateDiff(calback)
        timeSlot.clear()
        timeSlot.addAll(slots)
        // diffRes.dispatchUpdatesTo(this) //TODO Diff Util does not work correctly
        notifyDataSetChanged()
    }

    class EmptySlotViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            view.timeTv.text = timeSlot.timeLable
        }
    }

    class BusySlotWithLoginViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setMyBookingView(timeSlot: TimeSlot) {
            if (timeSlot.isOtherBooking)
                view.userIndex.setBackgroundResource(R.color.other_booking_colour)
            else if (timeSlot.isMyBooking)
                view.userIndex.setBackgroundResource(R.color.my_booking_colour)
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
            else if (timeSlot.isMyBooking)
                view.userIndex.setBackgroundResource(R.color.my_booking_colour)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            R.layout.empty_time_slot -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.empty_time_slot, parent, false)
                EmptySlotViewHolder(view)
            }
            R.layout.busy_time_slot_with_login_item -> {
                view =
                    LayoutInflater.from(parent.context).inflate(R.layout.busy_time_slot_with_login_item, parent, false)
                BusySlotWithLoginViewHolder(view)
            }
            else -> {
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
            getItemViewType(position) == R.layout.busy_time_slot_with_login_item -> (viewHolder as BusySlotWithLoginViewHolder).setMyBookingView(
                timeSlot[position]
            )
            getItemViewType(position) == R.layout.empty_time_slot -> (viewHolder as EmptySlotViewHolder).setMyBookingView(
                timeSlot[position]
            )
            else -> (viewHolder as BusySlotNoLoginViewHolder).setMyBookingView(timeSlot[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        val date = DateTime(timeSlot[position].range.first)
        return when {
            (timeSlot[position].isMyBooking || timeSlot[position].isOtherBooking) && !timeSlot[position].isContinue -> R.layout.busy_time_slot_with_login_item
            timeSlot[position].isContinue && date.hourOfDay == FIRST_HOUR && date.minuteOfHour == 0 -> R.layout.busy_time_slot_with_login_item
            timeSlot[position].isContinue -> R.layout.busy_time_slot_no_login_item
            else -> R.layout.empty_time_slot
        }
    }
}
