package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.domainapi.model.SessionData
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.chooseTime.ChooseTimeSharedViewModel
import java.text.SimpleDateFormat

class BackDeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_back_device, container, false)
        val textView: TextView = view.findViewById(R.id.dateToBack)
        val button: Button = view.findViewById(R.id.giveBackBut)

        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)
        val modelBackDevice = ViewModelProviders.of(activity!!).get(BackDeviceViewModel::class.java)

        modelBackDevice.getSessionData().observe(this, Observer {
            it?.let {
                val format = SimpleDateFormat("HH:mm\ndd MMMM")
                textView.setText(format.format(it.endData.time))
            }
            Toast.makeText(context, "WTF", Toast.LENGTH_LONG).show()
        })

        modelChooseTime.choosenData.value?.let {
            val endDate = modelChooseTime.choosenData.value
            val userId = modelChooseTime.userId.value
            modelBackDevice.setBookingSession(SessionData(userId!!, endDate!!))
        }

        button.setOnClickListener {

            modelBackDevice.tryReturnDevice().observe(this, Observer {
                Toast.makeText(context, "is device returned $it", Toast.LENGTH_LONG).show()
                if (it) {
                    Navigation.findNavController(view).navigate(R.id.action_backDeviceFragment_to_loginFragment)
                }
            })
        }
        return view
    }
}