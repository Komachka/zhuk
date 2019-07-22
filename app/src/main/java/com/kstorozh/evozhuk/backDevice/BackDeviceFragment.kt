package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.chooseTime.ChooseTimeSharedViewModel
import com.kstorozh.evozhuk.login.LogInViewModel
import java.text.SimpleDateFormat


class BackDeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val modelLogin = ViewModelProviders.of(activity!!).get(LogInViewModel::class.java)
        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)
        val view: View = inflater.inflate(R.layout.fragment_back_device, container, false)

        val textView:TextView = view.findViewById(R.id.dateToBack)
        val format = SimpleDateFormat("dd MMMMM\nHH:mm")
        textView.setText(format.format(modelChooseTime.choosenData.value?.time))
        val button: Button = view.findViewById(R.id.giveBackBut)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_backDeviceFragment_to_loginFragment)
        }
        return view
    }
}