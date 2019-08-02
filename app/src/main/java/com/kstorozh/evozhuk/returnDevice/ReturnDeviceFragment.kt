package com.kstorozh.evozhuk.returnDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstorozh.evozhuk.R

import android.os.Handler
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.kstorozh.evozhuk.DELY_NAVIGATION_ERROR
import com.kstorozh.evozhuk.LOG_TAG
import com.kstorozh.evozhuk.TIME_TO_WAIT
import com.kstorozh.evozhuk.utils.getDeviceName
import kotlinx.android.synthetic.main.logo_and_info.view.*
import java.lang.Exception

class ReturnDeviceFragment : Fragment(), BatteryLevelCheck {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_return_device, container, false)
        fragment.deviceNameTv.text = context?.getDeviceName()
        fragment.manageBatteryCharge()
            Handler().postDelayed({
                try {
                    val navController = this.findNavController()
                    navController.navigateUp()
                } catch (e: Exception) {
                    Log.d(LOG_TAG, DELY_NAVIGATION_ERROR)
                }
            }, TIME_TO_WAIT)
        return fragment
    }
}
