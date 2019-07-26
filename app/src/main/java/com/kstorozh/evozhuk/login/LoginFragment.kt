package com.kstorozh.evozhuk.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R
import androidx.lifecycle.ViewModelProviders
import android.widget.EditText
import com.kstorozh.evozhuk.getDeviceName
import com.kstorozh.evozhuk.getInfoAboutDevice

import com.kstorozh.evozhuk.showSnackbar
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment(), RemindPinDialog, UserNamesDataHandler {

    // TODO move it inside method

    lateinit var model: LogInViewModel
    lateinit var loginBut: Button
    lateinit var loginEt: AutoCompleteTextView
    lateinit var passEt: EditText
    lateinit var forgotPassTv: TextView

    lateinit var userNames: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment: View = inflater.inflate(R.layout.fragment_login, container, false)
        fragment.findViewById<ImageView>(R.id.infoImageBut)
            .setOnClickListener {
                Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_infoFragment)
            }

        fragment.findViewById<TextView>(R.id.deviceNameTv).text = context.getDeviceName()
        loginBut = fragment.findViewById(R.id.goInBut)
        loginEt = fragment.findViewById(R.id.loginEt) as AutoCompleteTextView
        passEt = fragment.findViewById(R.id.passwordEt) as EditText
        forgotPassTv = fragment.findViewById(R.id.forgotPassTv)
        forgotPassTv.setOnClickListener { show() }
        model = ViewModelProviders.of(this)[LogInViewModel::class.java]
        subscribeNamesLiveData()

        model.isDeviceBooked(context.getInfoAboutDevice()).observe(this, Observer {
            if (it) Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_backDeviceFragment)
        })

        loginBut.setOnClickListener { view ->

            if (passEt.text.isNotEmpty() && loginEt.text.isNotEmpty())
                    model.tryLogin(loginEt.text.toString(), passEt.text.toString()).observe(this, Observer {
                        if (!it.isNullOrEmpty()) {
                            setEmptyValues()
                            val action = LoginFragmentDirections.actionLoginFragmentToChooseTimeFragment(it)
                            Navigation.findNavController(fragment).navigate(action)
                        } else {
                            view.showSnackbar(resources.getString(R.string.invalid_pass_error_message))
                        }
                })
            else
                view.showSnackbar(resources.getString(R.string.pass_is_empty_error_message))
        }
        return fragment
    }

    private fun setEmptyValues() {
        loginEt.setText("")
        passEt.setText("")
    }
}
