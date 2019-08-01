package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import java.util.*

import android.os.Build
import com.kstorozh.evozhuk.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*

class SpecificTimeAndDateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)
        val curentTimeAndDate = CustomTime()
        curentTimeAndDate.countMinutesWithInterval(TIME_PICKER_INTERVAL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.timePicker.hour = curentTimeAndDate.hour
            fragment.timePicker.minute = (curentTimeAndDate.minute)
        } else {
            fragment.timePicker.setCurrentHour(curentTimeAndDate.hour)
            fragment.timePicker.setCurrentMinute(curentTimeAndDate.minute)
        }

        fragment.timePicker.setOnTimeChangedListener { timePicker, pickerHour, pickerMinute ->
            curentTimeAndDate.hour = pickerHour
            curentTimeAndDate.minute = pickerMinute * TIME_PICKER_INTERVAL // back to real time
        }

        fragment.datePicker.init(curentTimeAndDate.year, curentTimeAndDate.month, curentTimeAndDate.day) { datePicker, pickYear, pickMonth, pickDay ->
            curentTimeAndDate.year = pickYear
            curentTimeAndDate.month = pickMonth
            curentTimeAndDate.day = pickDay
        }

        (activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
        fragment.toolbar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
            title = resources.getString(R.string.time_choose_tool_bar)
            setNavigationOnClickListener {
                val action = SpecificTimeAndDateFragmentDirections.actionSpecificTimeAndDateToChooseTimeFragment(
                    USER_ID_NOT_SET)
                action.milisec = curentTimeAndDate.getMillisec()
                Navigation.findNavController(fragment).navigate(action)
            }
        }
        return fragment
    }
}
