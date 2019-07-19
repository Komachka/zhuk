package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

class ChooseTimeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_time_choose, container, false)
        val button: Button = view.findViewById(R.id.takeDevice)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_backDeviceFragment)
        }
        return view
        }
}