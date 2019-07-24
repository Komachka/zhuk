package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class ChooseTimeFragment : Fragment() {

    var selectedButton: Button?=null
        private set(value) {
            field = value
            value!!.setBackgroundResource(R.drawable.time_but_pressed)
            value.setTextColor(getResources().getColor(R.color.but_time_def))

        }

    private lateinit var calendar: GregorianCalendar
    private var milisec:Long = 0
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        milisec = ChooseTimeFragmentArgs.fromBundle(arguments!!).milisec
        if(milisec == 0L)
            milisec = System.currentTimeMillis() + 3600000L * 4
        //calendar = GregorianCalendar.getInstance() as GregorianCalendar
        //calendar.timeZone = TimeZone.getTimeZone("Europe/Kiev")
        //calendar.set(Calendar.HOUR, 4)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)
        val modelLogin = ViewModelProviders.of(activity!!).get(LogInViewModel::class.java)

        modelLogin.userIdLiveData.value?.let {
            userId = modelLogin.userIdLiveData.value!!
            modelChooseTime.setUserId(modelLogin.userIdLiveData.value!!) }


        val view: View = inflater.inflate(R.layout.fragment_time_choose, container, false)
        val button: Button = view.findViewById(R.id.takeDevice)

        val buttonList = listOf<Button>(
            view.findViewById(R.id.oneHourBut),
            view.findViewById(R.id.twoHourBut),
            (view.findViewById(R.id.fourHourBut) as Button).also {
                selectedButton = it
            },
            view.findViewById(R.id.allDayBut),
            view.findViewById(R.id.twoDaysBut),
            (view.findViewById(R.id.anotherTimeBut) as Button).also {
                if (ChooseTimeFragmentArgs.fromBundle(arguments!!).milisec != 0L) {
                    selectedButton = it
                }
            }
        )

        buttonList.forEach {
            it.setOnClickListener {
                (it as Button).setBackgroundResource(R.drawable.time_but_pressed)
                it.setTextColor(getResources().getColor(R.color.but_time_focus))
                selectedButton?.let {
                    resetButton(it)
                }
                selectedButton = it
                /*calendar = GregorianCalendar.getInstance() as GregorianCalendar
                calendar.timeZone = TimeZone.getTimeZone("Europe/Kiev")
                Log.d("MainActivity", calendar.time.toString())
                    when ((it as Button).id) {
                        R.id.oneHourBut -> calendar.add(Calendar.HOUR, 1)
                        R.id.twoHourBut -> calendar.add(Calendar.HOUR, 2)
                        R.id.fourHourBut -> calendar.add(Calendar.HOUR, 4)
                        R.id.twoDaysBut -> calendar.add(Calendar.HOUR, 48)
                        R.id.allDayBut ->
                        {
                            calendar.add(Calendar.DATE, 1)
                            calendar =  GregorianCalendar(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DATE), 19, 0, 0);
                            calendar.timeZone = TimeZone.getTimeZone("Europe/Kiev")

                        }

                        else -> {
                            Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_specificTimeAndDate)
                        }
                    }*/


                //Log.d("MainActivity", calendar.time.toString())
                milisec =     when ((it as Button).id) {
                        R.id.oneHourBut -> System.currentTimeMillis() + 3600000L
                        R.id.twoHourBut -> System.currentTimeMillis() + 3600000L * 2
                        R.id.fourHourBut -> System.currentTimeMillis() + 3600000L * 4
                        R.id.twoDaysBut -> System.currentTimeMillis() + 3600000L * 48
                        R.id.allDayBut ->
                        {

                            val currentime = GregorianCalendar.getInstance()
                            val mCalendar =  GregorianCalendar(currentime.get(Calendar.YEAR),
                                currentime.get(Calendar.MONTH),
                                currentime.get(Calendar.DAY_OF_MONTH), 19, 0, 0);
                            mCalendar.timeZone = TimeZone.getTimeZone("Europe/Kiev")
                            mCalendar.timeInMillis
                        }

                        else -> {
                            Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_specificTimeAndDate)
                            0
                        }
                    }


                //calendar.let { it1 -> modelChooseTime.setData(it1) }

            }
        }

        button.setOnClickListener {
            Log.d("MainActivity", SimpleDateFormat("hh:mm dd MMMM").format(milisec))
            /*modelChooseTime.tryBookDevice(calendar, userId).observe(this, androidx.lifecycle.Observer {
                if (it == true) {
                    Toast.makeText(context, "Device successfully booked", Toast.LENGTH_LONG).show()
                    modelLogin.userIdLiveData.value = null // TODO we do this to if we go back to login screen don navigate to took device always
                    Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_backDeviceFragment)
                } else {
                    Toast.makeText(context, "Can not book the device", Toast.LENGTH_LONG).show()
                }
            })*/
        }
        return view
        }

    private fun resetButton(button: Button) {
        button.setBackgroundResource(R.drawable.round_rectangle)
        button.setTextColor(getResources().getColor(R.color.but_time_def))
    }

}