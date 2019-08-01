package com.kstorozh.evozhuk.chooseTime

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import com.kstorozh.evozhuk.TIME_PICKER_INTERVAL
import java.util.ArrayList

class TimePickerMinInterval : TimePicker {
    lateinit var minutePicker: NumberPicker

    constructor(context: Context) : super(context) { initContext() }
    constructor(context: Context, attr: AttributeSet) : super(context, attr) { initContext() }
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr) { initContext() }

    private fun initContext() {
        setIs24HourView(true)
        try {

            minutePicker = this.findViewById(
                Resources.getSystem().getIdentifier("minute", "id", "android")
            ) as NumberPicker
            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL - 1
            val displayedValues = ArrayList<String>()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) {
            Log.e("MainActivity", "Exception: $e")
        }
    }

    override fun setCurrentMinute(currentMinute: Int) {
        super.setCurrentMinute(currentMinute / TIME_PICKER_INTERVAL)
    }

    override fun setMinute(minute: Int) {
        super.setMinute(minute / TIME_PICKER_INTERVAL)
    }
}