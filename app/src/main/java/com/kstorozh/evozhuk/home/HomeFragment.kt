package com.kstorozh.evozhuk.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.ErrorViewModel
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.getInfoAboutDevice

class HomeFragment : Fragment() {

    val LOG_TAG = "MainActivity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_at_home, container, false)

        val model = ViewModelProviders.of(this)[HomeViewModel::class.java]
        val errorModel = ViewModelProviders.of(activity!!).get(ErrorViewModel::class.java)

        Log.d(LOG_TAG, context.getInfoAboutDevice().toString())
        val info = context.getInfoAboutDevice()
        val welcomeMessageTv = view.findViewById<TextView>(R.id.welcomeMessageTv)
        welcomeMessageTv.setText(info.model)

        model.isDeviceInited(info).observe(
            this, Observer {
                Log.d(LOG_TAG, "isDeviceInited =   $it")
                if (it)
                    Toast.makeText(this.context, "Device is registered in database.", Toast.LENGTH_LONG).show()
            }
        )

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
