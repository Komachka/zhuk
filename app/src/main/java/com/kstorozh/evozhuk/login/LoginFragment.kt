package com.kstorozh.evozhuk.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

import androidx.lifecycle.ViewModelProviders


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val but: Button = view.findViewById(R.id.goInBut)

        val login = view.findViewById(R.id.loginEt) as AutoCompleteTextView
        val pass:EditText = view.findViewById(R.id.passwordEt) as EditText

        val model = ViewModelProviders.of(this)[LogInViewModel::class.java]



        val mCats = arrayOf("thesickboii", "dmitriy.senchik","katya", "petro", "vasa", "boris", "natasha", "ibragim", "qwerty", "qwertyqwe", "qwerty67868")
        login.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, mCats))

        /*model.getUsers().observe(this, Observer {
            mAutoCompleteTextView.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, it))
        })*/



        but.setOnClickListener {

            if (mCats.contains<String>(login.text.toString().toLowerCase()) && pass.text.isNotEmpty())

                model.tryLogin(login.text.toString(), pass.text.toString()).observe(this, Observer {
                    Toast.makeText(context, it?:"Does not logined", Toast.LENGTH_LONG).show()
                })
            else
                Toast.makeText(context, "Wrong login", Toast.LENGTH_LONG).show()
        }


        model.userIdLiveData.observe(this, Observer {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_chooseTimeFragment)
        })

        return view
    }
}
