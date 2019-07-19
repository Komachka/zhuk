package com.kstorozh.evozhuk.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val but: Button = view.findViewById(R.id.goInBut)
        but.setOnClickListener {

            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_chooseTimeFragment)
        }
        return view
    }
}
