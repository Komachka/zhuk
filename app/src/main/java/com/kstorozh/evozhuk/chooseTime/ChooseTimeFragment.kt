package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.ErrorViewModel
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.login.LogInViewModel
import java.util.*

class ChooseTimeFragment : Fragment() {

    var selectedButton: Button? = null
    lateinit var calendar: GregorianCalendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val errorModel = ViewModelProviders.of(activity!!).get(ErrorViewModel::class.java)

        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)
        val modelLogin = ViewModelProviders.of(activity!!).get(LogInViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_time_choose, container, false)
        val button: Button = view.findViewById(R.id.takeDevice)

        val buttonList = listOf<Button>(
            view.findViewById(R.id.oneHourBut),
            view.findViewById(R.id.twoHourBut),
            view.findViewById(R.id.fourHourBut),
            view.findViewById(R.id.allDayBut),
            view.findViewById(R.id.twoDaysBut),
            view.findViewById(R.id.anotherTimeBut)
        )

        buttonList.forEach {
            it.setOnClickListener {
                (it as Button).setBackgroundResource(R.drawable.time_but_pressed)
                it.setTextColor(getResources().getColor(R.color.but_time_focus))
                selectedButton?.let {
                    it.setBackgroundResource(R.drawable.round_rectangle)
                    it.setTextColor(getResources().getColor(R.color.but_time_def))
                }
                selectedButton = it
                calendar = GregorianCalendar.getInstance() as GregorianCalendar
                calendar.timeZone = TimeZone.getTimeZone("EEST")
                Log.d("MainActivity", calendar.time.toString())
                    when ((it as Button).id) {
                        R.id.oneHourBut -> calendar.add(Calendar.HOUR, 1)
                        R.id.twoHourBut -> calendar.add(Calendar.HOUR, 2)
                        R.id.fourHourBut -> calendar.add(Calendar.HOUR, 4)
                        R.id.allDayBut ->
                        {
                            calendar.add(Calendar.DATE, 1)
                        }
                        R.id.twoDaysBut -> calendar.add(Calendar.DATE, 2)
                        else -> calendar.add(Calendar.SECOND, 1) // TODO change it later
                    }
                calendar?.let { it1 -> modelChooseTime.setData(it1) }
                modelLogin.userIdLiveData.value?.let { modelChooseTime.setUserId(modelLogin.userIdLiveData.value!!) }
            }
        }

        button.setOnClickListener {
            modelChooseTime.tryBookDevice().observe(this, androidx.lifecycle.Observer {

                if (it == true) {
                    Toast.makeText(context, "Device successfully booked", Toast.LENGTH_LONG).show()
                    modelLogin.userIdLiveData.value = null // TODO we do this to if we go back to login screen don navigate to took device always
                    Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_backDeviceFragment)
                } else {
                    Toast.makeText(context, "Can not book the device", Toast.LENGTH_LONG).show()
                }
            })
        }
        return view
        }
}