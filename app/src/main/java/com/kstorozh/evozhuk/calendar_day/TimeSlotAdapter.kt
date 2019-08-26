package com.kstorozh.evozhuk.calendar_day

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.FIRST_HOUR
import com.kstorozh.evozhuk.LOG_TAG
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.empty_time_slot.view.timeTv
import kotlinx.android.synthetic.main.busy_time_slot_with_login_item.view.*
import org.joda.time.DateTime

class TimeSlotAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val EMPTY_VIEW_TYPE = 0
    private val BOOKING_VIEW_TYPE = 1
    private val CONTINUE_BOOKING_VIEW_TYPE = 2


    private val timeSlot  = mutableListOf<TimeSlot>()
    inner class SlotsDiffCallback(private val oldList:List<TimeSlot>, private val newList: List<TimeSlot>): DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val t = oldList[oldItemPosition].timeLable == newList[newItemPosition].timeLable
            Log.d(LOG_TAG, "are items the same $t")
            return t
        }

        override fun getOldListSize(): Int {
            Log.d(LOG_TAG, "old size ${oldList.size}")
            return oldList.size
        }

        override fun getNewListSize(): Int {
            Log.d(LOG_TAG, "new size ${newList.size}")
            return  newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val r =  oldList[oldItemPosition] == newList[newItemPosition]
            Log.d(LOG_TAG, "are content are the same $r")
            return r
        }
    }

    fun updateData(slots: List<TimeSlot>) {
        val calback = SlotsDiffCallback(timeSlot, slots)
        val diffRes = DiffUtil.calculateDiff(calback)
        timeSlot.clear()
        timeSlot.addAll(slots)
        //diffRes.dispatchUpdatesTo(this) //TODO doe not work
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
            EMPTY_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.empty_time_slot, parent, false)
                EmptySlotViewHolder(view)
            }
            BOOKING_VIEW_TYPE -> {
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
            getItemViewType(position) == BOOKING_VIEW_TYPE -> (viewHolder as BusySlotWithLoginViewHolder).setMyBookingView(
                timeSlot[position]
            )
            getItemViewType(position) == EMPTY_VIEW_TYPE -> (viewHolder as EmptySlotViewHolder).setMyBookingView(
                timeSlot[position]
            )
            else -> (viewHolder as BusySlotNoLoginViewHolder).setMyBookingView(timeSlot[position])
        }
    }


    override fun getItemViewType(position: Int): Int {
        val date = DateTime(timeSlot[position].range.first)
        return when {
            (timeSlot[position].isMyBooking || timeSlot[position].isOtherBooking) && !timeSlot[position].isContinue -> BOOKING_VIEW_TYPE
            timeSlot[position].isContinue && date.hourOfDay == FIRST_HOUR && date.minuteOfHour == 0 -> BOOKING_VIEW_TYPE
            timeSlot[position].isContinue -> CONTINUE_BOOKING_VIEW_TYPE
            else -> EMPTY_VIEW_TYPE
        }
    }
}





