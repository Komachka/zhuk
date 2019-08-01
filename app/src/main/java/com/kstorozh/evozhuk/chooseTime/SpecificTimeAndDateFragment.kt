package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import android.os.Build
import com.kstorozh.evozhuk.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*

class SpecificTimeAndDateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)
    }

    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        val currentTimeAndDate = CustomTime()
        currentTimeAndDate.countMinutesWithTimePickerInterval(TIME_PICKER_INTERVAL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.timePicker.hour = currentTimeAndDate.hour
            fragment.timePicker.minute = (currentTimeAndDate.minute)
        } else {
            fragment.timePicker.setCurrentHour(currentTimeAndDate.hour)
            fragment.timePicker.setCurrentMinute(currentTimeAndDate.minute)
        }
        fragment.timePicker.setOnTimeChangedListener { timePicker, pickerHour, pickerMinute ->
            currentTimeAndDate.hour = pickerHour
            currentTimeAndDate.minute = pickerMinute * TIME_PICKER_INTERVAL // back to real time
        }
        fragment.datePicker.init(currentTimeAndDate.year, currentTimeAndDate.month, currentTimeAndDate.day) { datePicker, pickYear, pickMonth, pickDay ->
            currentTimeAndDate.year = pickYear
            currentTimeAndDate.month = pickMonth
            currentTimeAndDate.day = pickDay
        }
        (activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
        fragment.toolbar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
            title = resources.getString(R.string.time_choose_tool_bar)
            setNavigationOnClickListener {
                val action = SpecificTimeAndDateFragmentDirections.actionSpecificTimeAndDateToChooseTimeFragment(
                    USER_ID_NOT_SET)
                action.milisec = currentTimeAndDate.getMillisec()
                Navigation.findNavController(fragment).navigate(action)
            }
        }
    }
}
