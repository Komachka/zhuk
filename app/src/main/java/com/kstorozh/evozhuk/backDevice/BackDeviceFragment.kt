package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

class BackDeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_back_device, container, false)
        val button: Button = view.findViewById(R.id.giveBackBut)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_backDeviceFragment_to_loginFragment)
        }
        return view
    }
}