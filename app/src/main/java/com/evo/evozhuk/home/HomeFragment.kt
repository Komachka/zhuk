package com.evo.evozhuk.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.evo.domainapi.model.DeviceInputData
import com.evo.evozhuk.*
import com.evo.evozhuk.utils.getInfoAboutDevice
import com.evo.evozhuk.utils.observe
import com.evo.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_at_home.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class HomeFragment : Fragment(), HandleErrors, KoinComponent {

    private val model: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_at_home, container, false)
    }

    private fun View.initDevice(deviceInputData: DeviceInputData) {
        viewLifecycleOwner.observe(model.initDevice(deviceInputData)) {
            if (it) {
                val message = resources.getString(R.string.device_registered_message)
                this.showSnackbar(message)
                Navigation.findNavController(this).navigate(R.id.action_homeFragment_to_loginFragment)
            } else this.showSnackbar(context.resources.getString(R.string.can_not_init_error_message))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
