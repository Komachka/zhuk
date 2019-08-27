package com.kstorozh.evozhuk.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.getInfoAboutDevice
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_at_home.view.*

class HomeFragment : Fragment(), HandleErrors {

    private lateinit var model: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_at_home, container, false)
    }

    private fun View.initDevice(deviceInputData: DeviceInputData) {
        Log.d("MainActivity", "deviceInputData $deviceInputData")
        viewLifecycleOwner.observe(model.initDevice(deviceInputData)) {
            if (it) {
                val message = resources.getString(R.string.device_registered_message)
                this.showSnackbar(message)
                Navigation.findNavController(this).navigate(R.id.action_homeFragment_to_loginFragment)
            } else this.showSnackbar(context.resources.getString(R.string.can_not_init_error_message))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model = ViewModelProviders.of(this)[HomeViewModel::class.java]
        viewLifecycleOwner.handleErrors(model, view)
        val info = context?.applicationContext!!.getInfoAboutDevice()
        view.welcomeMessageTv.text = info.model
        viewLifecycleOwner.observe(model.isDeviceInited(info), {
            if (it) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment)
            }
        })
        view.initBut.setOnClickListener {
            view.initDevice(info)
        }
    }
}
