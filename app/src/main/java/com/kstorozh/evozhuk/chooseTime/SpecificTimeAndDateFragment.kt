package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import android.os.Build
import androidx.lifecycle.ViewModelProviders
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.timePicker
import org.joda.time.DateTime

class SpecificTimeAndDateFragment : Fragment(), HandleErrors {

    private lateinit var currentTimeAndDate: CustomTime
    private lateinit var model: SpecificTimeAndDateViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)
    }

    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        model = ViewModelProviders.of(this)[SpecificTimeAndDateViewModel::class.java]
        viewLifecycleOwner.handleErrors(model, fragment)
        currentTimeAndDate = CustomTime()
        arguments?.let {
            var currentMs = SpecificTimeAndDateFragmentArgs.fromBundle(it).currentMilisec
            currentMs = if (currentMs < System.currentTimeMillis()) System.currentTimeMillis() else currentMs
            val maxMs = SpecificTimeAndDateFragmentArgs.fromBundle(it).maxMilisec
            val currentDt = DateTime(currentMs)
            val maxDt = DateTime(maxMs)
            currentTimeAndDate.year = currentDt.year
            currentTimeAndDate.month = currentDt.monthOfYear - 1
            currentTimeAndDate.day = currentDt.dayOfMonth
            currentTimeAndDate.hour = currentDt.hourOfDay
            currentTimeAndDate.minute = currentDt.minuteOfHour
            currentTimeAndDate.seconds = currentDt.secondOfMinute
            datePicker.maxDate = maxDt.millis
            datePicker.minDate = System.currentTimeMillis()
        }

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
        fragment.datePicker.init(
            currentTimeAndDate.year,
            currentTimeAndDate.month,
            currentTimeAndDate.day
        ) { datePicker, pickYear, pickMonth, pickDay ->
            currentTimeAndDate.year = pickYear
            currentTimeAndDate.month = pickMonth
            currentTimeAndDate.day = pickDay
        }
        (activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
        fragment.toolbar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
            title = resources.getString(R.string.time_choose_tool_bar)
            setNavigationOnClickListener { Navigation.findNavController(fragment).popBackStack() }
        }
        fragment.chooseTimeBut.setOnClickListener { fragment.navigateBackWithTime() }
    }

    private fun View.navigateBackWithTime() {
        val view = this
        arguments?.let {
            if (SpecificTimeAndDateFragmentArgs.fromBundle(it).backDiraction == CHOOSE_TIME_FRAGMENT_DIR) {
                val action =
                    SpecificTimeAndDateFragmentDirections.actionSpecificTimeAndDateToChooseTimeFragment(USER_ID_NOT_SET)
                action.milisec = currentTimeAndDate.getMillisec()
                Navigation.findNavController(view).navigate(action)
            } else if (SpecificTimeAndDateFragmentArgs.fromBundle(it).backDiraction == BACK_DEVICE_FRAGMENT_DIR) {
                viewLifecycleOwner.observe(model.editCurrentBooking(currentTimeAndDate.getMillisec())) {
                    if (it) {
                        val action = SpecificTimeAndDateFragmentDirections
                            .actionSpecificTimeAndDateToBackDeviceFragment(
                                USER_ID_NOT_SET,
                                currentTimeAndDate.getMillisec()
                            )
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
        }
    }
}
