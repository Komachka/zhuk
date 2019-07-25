package com.kstorozh.evozhuk.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_at_home, container, false)
        val model = ViewModelProviders.of(this)[HomeViewModel::class.java]
        Log.d(LOG_TAG, context.getInfoAboutDevice().toString())
        val info = context.getInfoAboutDevice()
        val welcomeMessageTv = view.findViewById<TextView>(R.id.welcomeMessageTv)
        welcomeMessageTv.text = info.model

        model.isDeviceInited(info).observe(
            this, Observer {
                if (it) {
                    val message = resources.getString(R.string.device_registered_message)
                    view.showSnackbar(message)
                }
            }
        )

        // TODO Change to loader later
        val textView: TextView = view.findViewById(R.id.logoTv)
        textView.setOnClickListener {
            model.isDeviceBooked(info).observe(this, Observer {
                if (!it) Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment)
                else Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_backDeviceFragment)
            })
        }

        return view
    }
}
