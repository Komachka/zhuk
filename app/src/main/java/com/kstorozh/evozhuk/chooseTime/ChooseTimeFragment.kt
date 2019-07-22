package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.login.LogInViewModel
import java.util.*

class ChooseTimeFragment : Fragment() {

    var selectedButton: Button? = null
    lateinit var calendar:GregorianCalendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)

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
                    when((it as Button).id) {
                        R.id.oneHourBut -> calendar.add(Calendar.HOUR, 1)
                        R.id.twoHourBut -> calendar.add(Calendar.HOUR, 2)
                        R.id.fourHourBut -> calendar.add(Calendar.HOUR, 4)
                        R.id.allDayBut -> calendar.add(Calendar.HOUR, 9)
                        R.id.twoDaysBut -> calendar.add(Calendar.DATE, 1)
                        else -> calendar.add(Calendar.SECOND, 1) // TODO change it later
                    }
                calendar?.let { it1 -> modelChooseTime.setData(it1) }
            }
        }

        val modelLogin = ViewModelProviders.of(activity!!).get(LogInViewModel::class.java)
        button.setOnClickListener {
            val userId = modelLogin.userIdLiveData.value!!
            modelChooseTime.tryBookDevice(userId).observe(this, androidx.lifecycle.Observer {

                Toast.makeText(context, "Is device booked? $it", Toast.LENGTH_LONG).show()
                if (it == true)
                    Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_backDeviceFragment)
                else
                    Toast.makeText(context, "Can not book", Toast.LENGTH_LONG).show()
            })

        }
        return view
        }
}