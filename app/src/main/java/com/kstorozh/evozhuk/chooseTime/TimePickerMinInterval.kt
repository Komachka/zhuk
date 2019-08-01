package com.kstorozh.evozhuk.chooseTime

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import com.kstorozh.evozhuk.*
import java.util.ArrayList

class TimePickerMinInterval : TimePicker {
    constructor(context: Context) : super(context) { initContext() }
    constructor(context: Context, attr: AttributeSet) : super(context, attr) { initContext() }
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr) { initContext() }

    private fun initContext() {
        setIs24HourView(true)
        try {
            val minutePicker = this.findViewById(
                Resources.getSystem().getIdentifier(NAME, DEF_TYPE, PACKAGE)
            ) as NumberPicker
            minutePicker.minValue = 0
            minutePicker.maxValue = MINUETS_IN_HOUR / TIME_PICKER_INTERVAL - 1
            minutePicker.displayedValues = createMinutesValues().toTypedArray()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "$TIME_PICKER_ERROR $e")
        }
    }

    private fun createMinutesValues(): ArrayList<String> {
        val displayedValues = ArrayList<String>()
        var i = 0
        while (i < MINUETS_IN_HOUR) {
            displayedValues.add(String.format(MINUTES_FORMAT_TIME_PICKER, i))
            i += TIME_PICKER_INTERVAL
        }
        return displayedValues
    }

    override fun setCurrentMinute(currentMinute: Int) {
        super.setCurrentMinute(currentMinute / TIME_PICKER_INTERVAL)
    }

    override fun setMinute(minute: Int) {
        super.setMinute(minute / TIME_PICKER_INTERVAL)
    }
}