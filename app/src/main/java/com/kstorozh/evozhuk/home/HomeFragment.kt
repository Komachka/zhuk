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
import androidx.navigation.Navigation
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.evozhuk.R
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    val initDeviceUseCases: ManageDeviceUseCases by inject()
    val LOG_TAG = "MainActivity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_at_home, container, false)
        initDeviceUseCases.initDevice(DeviceInputData("007", "sumsung", "android", "s8", 300, 300)).observe(
            this, Observer {
                Log.d(LOG_TAG, "in init device result is $it")
                Toast.makeText(this.context, it.toString(), Toast.LENGTH_LONG).show()
            })
        val textView: TextView = view.findViewById(R.id.logoTv)
        textView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment)
        }
        return view
    }
}