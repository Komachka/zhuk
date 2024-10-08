package com.evo.evozhuk.chooseTime

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import com.evo.evozhuk.*
import com.evo.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.*
import kotlinx.android.synthetic.main.fragment_specific_time_and_date.view.*
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel

class SpecificTimeAndDateFragment : Fragment(), HandleErrors {

    private val model: SpecificTimeAndDateViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_time_and_date, container, false)
    }

    lateinit var dateTime: DateTime
    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.handleErrors(model, fragment)
        dateTime = DateTime()
        arguments?.let {
            var currentMs = SpecificTimeAndDateFragmentArgs.fromBundle(it).currentMilisec
            currentMs = if (currentMs < System.currentTimeMillis()) System.currentTimeMillis() else currentMs
            val maxMs = SpecificTimeAndDateFragmentArgs.fromBundle(it).maxMilisec
            var currentDt = DateTime(currentMs)
            if (currentDt.minuteOfHour % 15 != 0) {
                currentDt = currentDt.plusMinutes(15)
            }
            val maxDt = DateTime(maxMs)
            dateTime.withMillis(currentMs)
            time_and_date.setDefaultDate(currentDt.toDate())
            time_and_date.maxDate = maxDt.toDate()
            time_and_date.minDate = currentDt.toDate()
            time_and_date.mustBeOnFuture()
        }

        time_and_date.setStepMinutes(15)
        time_and_date.addOnDateChangedListener { displayed, date ->
            dateTime = dateTime.withMillis(date.time)
        }
        (activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
        fragment.toolbar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
            title = resources.getString(R.string.time_choose_tool_bar)
            setTitleTextColor(resources.getColor(R.color.logoTextColour))
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
