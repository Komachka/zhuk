package com.kstorozh.evozhuk.returnDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.getDeviceName
import com.kstorozh.evozhuk.getInfoAboutDevice

const val TIME_TO_WAIT = 10000L // 10 sec

class ReturnDeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_return_device, container, false)
        fragment.findViewById<TextView>(R.id.deviceNameTv).text = context.getDeviceName()
        Handler().postDelayed({
            val navController = this.findNavController()
            navController.navigateUp()
        }, TIME_TO_WAIT)

        return fragment
    }
}
