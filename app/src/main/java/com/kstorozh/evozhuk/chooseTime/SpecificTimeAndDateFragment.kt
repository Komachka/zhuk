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
import java.util.*


class SpecificTimeAndDateFragment : Fragment() {


    companion object Time{
        var year = 0
        var month = 0
        var day = 0
        var hour = 0
        var minute = 0
        var seconds = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment =  inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)

        val dateAndTimeNow = Calendar.getInstance()
        dateAndTimeNow.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"))

        Time.year = dateAndTimeNow.get(Calendar.YEAR);
        Time.month = dateAndTimeNow.get(Calendar.MONTH);
        Time.day = dateAndTimeNow.get(Calendar.DAY_OF_MONTH);
        Time.hour = dateAndTimeNow.get(Calendar.HOUR_OF_DAY);
        Time.minute = dateAndTimeNow.get(Calendar.MINUTE);
        Time.seconds = dateAndTimeNow.get(Calendar.SECOND);

        val datePicker = fragment.findViewById<DatePicker>(R.id.datepicker)


        val timePicker = fragment.findViewById<TimePicker>(R.id.timepicker)
        timePicker.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.minute= minute
            timePicker.hour = hour
        }
        timePicker.setOnTimeChangedListener{timePicker, pickerHour, pickerMinute ->
            hour = pickerHour
            minute = pickerMinute
        }


        datePicker.init(year, month, day){datePicker, pickYear, pickMonth, pickDay ->
            year = pickYear
            month = pickMonth
            day = pickDay
        }


        val mToolbar = fragment.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
        mToolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp))
        mToolbar.title = "Выбор времени"
        mToolbar.setNavigationOnClickListener{
            val milisec = getMilisec()
            val action = SpecificTimeAndDateFragmentDirections.actionSpecificTimeAndDateToChooseTimeFragment()
            action.milisec = milisec
            Navigation.findNavController(fragment).navigate(action)
        }

        return fragment
    }

    private fun getMilisec(): Long {
        val calendar =  GregorianCalendar(year, month, day, hour, minute, seconds);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"))
        return calendar.time.time
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}
