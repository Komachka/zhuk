package com.kstorozh.evozhuk.chooseTime

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.kstorozh.evozhuk.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.USER_ID_NOT_SET
import java.util.*
import android.widget.NumberPicker

import android.content.res.Resources
import android.util.Log
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*


class SpecificTimeAndDateFragment : Fragment() {


    val TIME_PICKER_INTERVAL: Int = 15
    companion object Time {
        var year = 0
        var month = 0
        var day = 0
        var hour = 0
        var minute = 0
        var seconds = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)

        val dateAndTimeNow = Calendar.getInstance()
        dateAndTimeNow.timeZone = TimeUtils.getCurrentTimeZone()
        initTimeObject(dateAndTimeNow)

        //val datePicker = fragment.findViewById<DatePicker>(R.id.datePicker)
        //val timePicker = fragment.findViewById<TimePicker>(R.id.timePicker)

        fragment.timePicker.setIs24HourView(true)

        setTimePickerInterval(fragment.timePicker)


        if (minute % TIME_PICKER_INTERVAL != 0) {
            val minuteFloor = minute + TIME_PICKER_INTERVAL - minute % TIME_PICKER_INTERVAL
            minute = minuteFloor + if (minute == minuteFloor + 1) TIME_PICKER_INTERVAL else 0
            if (minute >= 60) {
                minute = minute % 60
                hour++
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragment.timePicker.hour = hour
                fragment.timePicker.minute = (minute / TIME_PICKER_INTERVAL)
            }
        }

        fragment.timePicker.setOnTimeChangedListener { timePicker, pickerHour, pickerMinute ->
            hour = pickerHour
            minute = pickerMinute
        }

        fragment.datePicker.init(year, month, day) { datePicker, pickYear, pickMonth, pickDay ->
            year = pickYear
            month = pickMonth
            day = pickDay
        }

        val mToolbar = fragment.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
        mToolbar.title = resources.getString(R.string.time_choose_tool_bar)
        mToolbar.setNavigationOnClickListener {
            val millisec = getMillisec()
            val action = SpecificTimeAndDateFragmentDirections.actionSpecificTimeAndDateToChooseTimeFragment(
                USER_ID_NOT_SET)
            action.milisec = millisec
            Navigation.findNavController(fragment).navigate(action)
        }

        return fragment
    }

    private fun initTimeObject(dateAndTimeNow: Calendar) {
        Time.year = dateAndTimeNow.get(Calendar.YEAR)
        Time.month = dateAndTimeNow.get(Calendar.MONTH)
        Time.day = dateAndTimeNow.get(Calendar.DAY_OF_MONTH)
        Time.hour = dateAndTimeNow.get(Calendar.HOUR_OF_DAY)
        Time.minute = dateAndTimeNow.get(Calendar.MINUTE)
        Time.seconds = dateAndTimeNow.get(Calendar.SECOND)
    }

    private fun getMillisec(): Long {
        val calendar = GregorianCalendar(year, month, day, hour, minute, seconds)
        calendar.timeZone = TimeUtils.getCurrentTimeZone()
        return calendar.time.time
    }


    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {

            val minutePicker = timePicker.findViewById(
                Resources.getSystem().getIdentifier(
                    "minute", "id", "android"
                )
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
}
