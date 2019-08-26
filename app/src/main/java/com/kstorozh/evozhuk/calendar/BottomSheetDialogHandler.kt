package com.kstorozh.evozhuk.calendar

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.LOG_TAG
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.TIME_PICKER_INTERVAL
import com.kstorozh.evozhuk.chooseTime.CustomTime
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import org.joda.time.DateTime
import androidx.appcompat.view.ContextThemeWrapper

interface BottomSheetDialogHandler {

    fun CalendarDayFragment.createBookingDialog(item: TimeSlot, userId: String) {
        if (this.context == null)
            return
        val mBottomSheetDialog = BottomSheetDialog(this.context!!)
        val sheetView = LayoutInflater.from(this.context).inflate(R.layout.bottom_sheet_dialog, null)
        val startDate = DateTime(item.range.first)
        val endDate = DateTime(item.range.last)

        val fromTimeAndDate = setTimeAndDate(sheetView.fromTimePicker, sheetView.fromDatePicker, startDate)
        val toTimeAndDate = setTimeAndDate(sheetView.toTimePicker, sheetView.toDatePicker, endDate)
        sheetView.bookBut.setOnClickListener {
            if (!item.isOtherBooking && !item.isMyBooking) {
                observe(model.createNewBooking(userId, fromTimeAndDate.getMillisec(), toTimeAndDate.getMillisec())) {
                    mBottomSheetDialog.cancel()
                }
            }
        }
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    fun CalendarDayFragment.editBookingDialog(item: TimeSlot, userId: String) {
        if (this.context == null)
            return
        val mBottomSheetDialog = BottomSheetDialog(this.context!!)
        val sheetView = LayoutInflater.from(this.context).inflate(R.layout.bottom_sheet_dialog, null)
        val startDate = DateTime(item.range.first)
        val endDate = DateTime(item.range.last)

        val fromTimeAndDate = setTimeAndDate(sheetView.fromTimePicker, sheetView.fromDatePicker, startDate)
        val toTimeAndDate = setTimeAndDate(sheetView.toTimePicker, sheetView.toDatePicker, endDate)
        sheetView.bookBut.setOnClickListener {
            if (item.isMyBooking) {
                observe(model.editBooking(userId, item.booking!!, fromTimeAndDate.getMillisec(), toTimeAndDate.getMillisec())) {
                    mBottomSheetDialog.cancel()
                }
            }
        }
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    fun CalendarDayFragment.deleteBookingDialog(item: TimeSlot, delete: (Booking) -> Unit) {
        if (this.context == null)
            return

        if (item.booking != null) {
            context?.applicationContext?.let {
                val alertDialog = AlertDialog.Builder(ContextThemeWrapper(context, R.style.myDialog))
                alertDialog.setTitle(resources.getString(R.string.delete_title))
                alertDialog.setPositiveButton(resources.getString(R.string.delete_booking)) { dialog, which ->
                    delete.invoke(item.booking!!)
                }
                alertDialog.setNegativeButton(resources.getString(R.string.dont_delete_booking)) { dialog, which ->
                }
                alertDialog.show()
            }
        }
    }

    fun setTimeAndDate(timePicker: TimePicker, datePicker: DatePicker, date: DateTime): CustomTime {
        Log.d(LOG_TAG, date.toString())
        val timeAndDate = CustomTime()
        timeAndDate.hour = date.hourOfDay
        timeAndDate.minute = date.minuteOfHour
        timeAndDate.day = date.dayOfMonth
        timeAndDate.month = date.monthOfYear - 1
        timeAndDate.year = date.year

        timeAndDate.countMinutesWithTimePickerInterval(TIME_PICKER_INTERVAL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = timeAndDate.hour
            timePicker.minute = (timeAndDate.minute)
        } else {
            timePicker.setCurrentHour(timeAndDate.hour)
            timePicker.setCurrentMinute(timeAndDate.minute)
        }
        timePicker.setOnTimeChangedListener { tP, pickerHour, pickerMinute ->
            timeAndDate.hour = pickerHour
            timeAndDate.minute = pickerMinute * TIME_PICKER_INTERVAL // back to real time
        }
        datePicker.init(timeAndDate.year, timeAndDate.month, timeAndDate.day) { dP, pickYear, pickMonth, pickDay ->
            timeAndDate.year = pickYear
            timeAndDate.month = pickMonth
            timeAndDate.day = pickDay
        }
        return timeAndDate
    }
}