package com.kstorozh.evozhuk.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.evozhuk.*
import kotlinx.android.synthetic.main.fragment_at_home.view.*

class HomeFragment : Fragment() {

    private lateinit var model: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_at_home, container, false)
        model = ViewModelProviders.of(this)[HomeViewModel::class.java]
        val info = context.getInfoAboutDevice()
        view.welcomeMessageTv.text = info.model
        observe(model.isDeviceInited(info), {
            if (it) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment)
            }
        })
        view.initBut.setOnClickListener {
            view.initDevice(info)
        }
        return view
    }

    private fun View.initDevice(deviceInputData: DeviceInputData) {
        observe(model.initDevice(deviceInputData)) {
            if (it) {
                val message = resources.getString(R.string.device_registered_message)
                this.showSnackbar(message)
                Navigation.findNavController(this).navigate(R.id.action_homeFragment_to_loginFragment)
            } else this.showSnackbar(context.resources.getString(R.string.can_not_init_error_message))
        }
    }
}
