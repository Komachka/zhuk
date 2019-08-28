package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*
import org.joda.time.DateTime
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import java.util.*


class SpecificTimeAndDateFragment : Fragment(), HandleErrors {

    //private lateinit var currentTimeAndDate: CustomTime
    private lateinit var model: SpecificTimeAndDateViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)
    }

    lateinit var dateTime:DateTime
    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        model = ViewModelProviders.of(this)[SpecificTimeAndDateViewModel::class.java]
        viewLifecycleOwner.handleErrors(model, fragment)
      //  currentTimeAndDate = CustomTime()

        arguments?.let {
            var currentMs = SpecificTimeAndDateFragmentArgs.fromBundle(it).currentMilisec
            currentMs = if (currentMs < System.currentTimeMillis()) System.currentTimeMillis() else currentMs
            val maxMs = SpecificTimeAndDateFragmentArgs.fromBundle(it).maxMilisec
            val currentDt = DateTime(currentMs)
            val maxDt = DateTime(maxMs)
           // currentTimeAndDate.year = currentDt.year
            //currentTimeAndDate.month = currentDt.monthOfYear - 1
            //currentTimeAndDate.day = currentDt.dayOfMonth
            //currentTimeAndDate.hour = currentDt.hourOfDay
            //currentTimeAndDate.minute = currentDt.minuteOfHour
            //currentTimeAndDate.seconds = currentDt.secondOfMinute
            time_and_date.setDefaultDate(Date(currentMs))
            time_and_date.maxDate = maxDt.toDate()
            time_and_date.minDate = Date()
        }


        time_and_date.setStepMinutes(15)
        time_and_date!!.addOnDateChangedListener { displayed, date ->
            dateTime = DateTime(date.time)
            Toast.makeText(context, dateTime.toString(), Toast.LENGTH_LONG).show()
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
                action.milisec = dateTime.millis
                Navigation.findNavController(view).navigate(action)
            } else if (SpecificTimeAndDateFragmentArgs.fromBundle(it).backDiraction == BACK_DEVICE_FRAGMENT_DIR) {
                viewLifecycleOwner.observe(model.editCurrentBooking(dateTime.millis)) {
                    if (it) {
                        val action = SpecificTimeAndDateFragmentDirections
                            .actionSpecificTimeAndDateToBackDeviceFragment(
                                USER_ID_NOT_SET,
                                dateTime.millis
                            )
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
        }
    }
}
