package com.kstorozh.evozhuk.calendar_day

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import org.joda.time.DateTime
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.fromDatePicker
import java.util.*

interface BottomSheetDialogHandler {

    fun ChildrenDayFragment.createBookingDialog(item: TimeSlot, userId: String) {
        if (this.context == null)
            return
        if (item.range.first < System.currentTimeMillis()) return
        val mBottomSheetDialog = BottomSheetDialog(this.context!!)

        val sheetView = LayoutInflater.from(this.context).inflate(R.layout.bottom_sheet_dialog, null)
        var startDate = DateTime(item.range.first)
        var endDate = DateTime(item.range.last).plusMinutes(1)

        sheetView.fromDatePicker.setDefaultDate(startDate.toDate())
        sheetView.fromDatePicker.setStepMinutes(15)
        sheetView.fromDatePicker.addOnDateChangedListener { displayed, date ->
            startDate = startDate.withMillis(date.time)
        }

        sheetView.toDatePicker.setDefaultDate(endDate.toDate())
        sheetView.toDatePicker.setStepMinutes(15)
        sheetView.toDatePicker.addOnDateChangedListener { displayed, date ->
            endDate = endDate.withMillis(date.time)
        }
        setUpMinAndMaxDate(item, sheetView)
        sheetView.bookBut.setOnClickListener {
            if (!item.isOtherBooking && !item.isMyBooking) {

                observe(model.createNewBooking(userId, startDate.millis, endDate.millis)) {
                    mBottomSheetDialog.cancel()
                }
            }
        }

        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    fun ChildrenDayFragment.editBookingDialog(item: TimeSlot, userId: String) {
        if (this.context == null)
            return
        val mBottomSheetDialog = BottomSheetDialog(this.context!!)

        val sheetView = LayoutInflater.from(this.context).inflate(R.layout.bottom_sheet_dialog, null)
        var startDate = DateTime(item.range.first)
        var endDate = DateTime(item.range.last)

        if (startDate.minuteOfHour % 15 != 0) {
            startDate = startDate.plusMinutes(15)
        }

        if (endDate.minuteOfHour % 15 != 0) {
            endDate = endDate.plusMinutes(15)
        }

        sheetView.fromDatePicker.setDefaultDate(startDate.toDate())
        sheetView.fromDatePicker.setStepMinutes(15)
        sheetView.fromDatePicker.addOnDateChangedListener { displayed, date ->
            startDate = startDate.withMillis(date.time)
        }

        sheetView.toDatePicker.setDefaultDate(endDate.toDate())
        sheetView.toDatePicker.setStepMinutes(15)
        sheetView.toDatePicker.addOnDateChangedListener { displayed, date ->
            endDate = endDate.withMillis(date.time)
        }

        setUpMinAndMaxDate(item, sheetView)

        sheetView.bookBut.setOnClickListener {
            if (item.isMyBooking) {
                observe(model.editBooking(userId, item.booking!!, startDate.millis, endDate.millis)) {
                    mBottomSheetDialog.cancel()
                }
            }
        }
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    fun ChildrenDayFragment.deleteBookingDialog(item: TimeSlot, delete: (Booking) -> Unit) {
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
}

private fun ChildrenDayFragment.setUpMinAndMaxDate(item: TimeSlot, view: View) {
    model.getNearbyBookings(item).observe(viewLifecycleOwner, Observer {

        if (it.first != null) {
            val minBooking = it.first!!
            if (minBooking != item.booking) {
                if (minBooking.endDate >= System.currentTimeMillis()) {
                    view.fromDatePicker.minDate = Date(minBooking.endDate)
                    view.toDatePicker.minDate = DateTime(minBooking.endDate).plusMinutes(15).toDate()
                } else {
                    view.fromDatePicker.minDate = Date()
                    view.toDatePicker.minDate = DateTime().plusMinutes(15).toDate()
                }
            } else {
                view.fromDatePicker.minDate = Date()
                view.toDatePicker.minDate = DateTime().plusMinutes(15).toDate()
            }
        } else {
            view.fromDatePicker.minDate = Date()
            view.toDatePicker.minDate = DateTime().plusMinutes(15).toDate()
        }

        if (it.second != null) {

            val maxBooking = it.second!!
            if (maxBooking != item.booking) {

                view.fromDatePicker.maxDate = DateTime(maxBooking.startDate).plusMinutes(-15).toDate()
                view.toDatePicker.maxDate = Date(maxBooking.startDate)
            } else {
                view.fromDatePicker.maxDate = DateTime().plusMinutes(-15).plusMonths(2).toDate()
                view.toDatePicker.maxDate = DateTime().plusMonths(2).toDate()
            }
        } else {
            view.fromDatePicker.maxDate = DateTime().plusMinutes(-15).plusMonths(2).toDate()
            view.toDatePicker.maxDate = DateTime().plusMonths(2).toDate()
        }
    })
}
