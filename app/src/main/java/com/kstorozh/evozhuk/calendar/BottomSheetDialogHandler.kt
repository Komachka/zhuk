package com.kstorozh.evozhuk.calendar

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kstorozh.evozhuk.LOG_TAG
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.TIME_PICKER_INTERVAL
import com.kstorozh.evozhuk.chooseTime.CustomTime
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*

interface BottomSheetDialogHandler {

    fun CalendarDayFragment.createDialog(item: TimeSlot, userId: String) {
        if (this.context == null)
            return
        val mBottomSheetDialog = BottomSheetDialog(this.context!!)
        val sheetView = LayoutInflater.from(this.context).inflate(R.layout.bottom_sheet_dialog, null)

        val fromTimeAndDate = setTimeAndDate(sheetView.fromTimePicker, sheetView.fromDatePicker)
        val toTimeAndDate = setTimeAndDate(sheetView.toTimePicker, sheetView.toDatePicker)
        sheetView.bookBut.setOnClickListener {
            if (!item.isOtherBooking && !item.isMyBooking) {
                observe(model.createNewBooking(userId, fromTimeAndDate.getMillisec(), toTimeAndDate.getMillisec())) {
                    Log.d(LOG_TAG, it.toString())
                }
            }
        }
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    fun setTimeAndDate(timePicker: TimePicker, datePicker: DatePicker): CustomTime {
        val timeAndDate = CustomTime()
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