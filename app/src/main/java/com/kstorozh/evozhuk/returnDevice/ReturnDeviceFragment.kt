package com.kstorozh.evozhuk.returnDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstorozh.evozhuk.R

import android.os.Handler
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.getDeviceName
import kotlinx.android.synthetic.main.logo_and_info.view.*

const val TIME_TO_WAIT = 10000L // 10 sec
const val BATTERY_LEVEL_TO_CHARGE = 50

class ReturnDeviceFragment : Fragment(), BatteryLevelCheck {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment = inflater.inflate(R.layout.fragment_return_device, container, false)
        fragment.deviceNameTv.text = context.getDeviceName()
        fragment.manageBatteryCharge()

        Handler().postDelayed({
            val navController = this.findNavController()
            navController.navigateUp()
        }, TIME_TO_WAIT)

        return fragment
    }
}
