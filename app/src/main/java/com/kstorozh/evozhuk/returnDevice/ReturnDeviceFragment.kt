package com.kstorozh.evozhuk.returnDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

import android.os.Handler

const val TIME_TO_WAIT = 10000L // 10 sec

class ReturnDeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_return_device, container, false)
        Handler().postDelayed({ Navigation.findNavController(fragment).navigate(R.id.action_returnDeviceFragment_to_loginFragment) }, TIME_TO_WAIT)

        return fragment
    }
}
