package com.kstorozh.evozhuk.calendar

import android.content.Context
import android.content.LocusId
import android.os.Build
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.TIME_PICKER_INTERVAL
import com.kstorozh.evozhuk.chooseTime.CustomTime
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*

interface BottomSheetDialogHandler {

    fun CalendarDayFragment.createDialog(item:TimeSlot, userId: String)
    {
        if (this.context == null)
            return
        val mBottomSheetDialog = BottomSheetDialog(this.context!!)
        val sheetView = LayoutInflater.from(this.context).inflate(R.layout.bottom_sheet_dialog, null)

        val fromTimeAndDate = CustomTime()
        fromTimeAndDate.countMinutesWithTimePickerInterval(TIME_PICKER_INTERVAL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sheetView.fromTimePicker.hour = fromTimeAndDate.hour
            sheetView.fromTimePicker.minute = (fromTimeAndDate.minute)
        } else {
            sheetView.fromTimePicker.setCurrentHour(fromTimeAndDate.hour)
            sheetView.fromTimePicker.setCurrentMinute(fromTimeAndDate.minute)
        }
        sheetView.fromTimePicker.setOnTimeChangedListener { timePicker, pickerHour, pickerMinute ->
            fromTimeAndDate.hour = pickerHour
            fromTimeAndDate.minute = pickerMinute * TIME_PICKER_INTERVAL // back to real time
        }
        sheetView.fromDatePicker.init(fromTimeAndDate.year, fromTimeAndDate.month, fromTimeAndDate.day) { datePicker, pickYear, pickMonth, pickDay ->
            fromTimeAndDate.year = pickYear
            fromTimeAndDate.month = pickMonth
            fromTimeAndDate.day = pickDay
        }

        val toTimeAndDate = CustomTime()
        toTimeAndDate.countMinutesWithTimePickerInterval(TIME_PICKER_INTERVAL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sheetView.toTimePicker.hour = toTimeAndDate.hour
            sheetView.toTimePicker.minute = (toTimeAndDate.minute)
        } else {
            sheetView.toTimePicker.setCurrentHour(toTimeAndDate.hour)
            sheetView.toTimePicker.setCurrentMinute(toTimeAndDate.minute)
        }
        sheetView.toTimePicker.setOnTimeChangedListener { timePicker, pickerHour, pickerMinute ->
            toTimeAndDate.hour = pickerHour
            toTimeAndDate.minute = pickerMinute * TIME_PICKER_INTERVAL // back to real time
        }
        sheetView.toDatePicker.init(toTimeAndDate.year, toTimeAndDate.month, toTimeAndDate.day) { datePicker, pickYear, pickMonth, pickDay ->
            toTimeAndDate.year = pickYear
            toTimeAndDate.month = pickMonth
            toTimeAndDate.day = pickDay
        }


        sheetView.bookBut.setOnClickListener {
            if (!item.isOtherBooking && !item.isMyBooking)
            {


                /*observe(model.createNewBooking(userId, fromTimeAndDate.getMillisec(), toTimeAndDate.getMillisec()))
                {

                }*/
            }
        }


        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()

    }
}