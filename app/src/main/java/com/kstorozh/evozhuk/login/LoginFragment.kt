package com.kstorozh.evozhuk.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.navigation.Navigation
import androidx.lifecycle.ViewModelProviders
import android.widget.EditText
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.getDeviceName
import com.kstorozh.evozhuk.utils.getInfoAboutDevice
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.logo_and_info.view.*

class LoginFragment : Fragment(), RemindPinDialog, UserNamesDataHandler {

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
        fragment.infoImageBut
            .setOnClickListener {
                Navigation.findNavController(fragment).navigate(LoginFragmentDirections.actionLoginFragmentToInfoFragment())
            }

        fragment.deviceNameTv.text = context?.getDeviceName()
        loginBut = fragment.goInBut
        loginEt = fragment.loginEt as AutoCompleteTextView
        passEt = fragment.passwordEt as EditText
        forgotPassTv = fragment.forgotPassTv
        forgotPassTv.setOnClickListener { show() }
        model = ViewModelProviders.of(this)[LogInViewModel::class.java]
        subscribeNamesLiveData()

        observe(model.errorViewModel) {
            var message: String = "error message"
            it.message?.let { message = it }
            if (it.message == null) it.throwable?.message?.let { message = it }
            activity?.currentFocus?.showSnackbar(message)
        }

        observe(model.isDeviceBooked(context?.applicationContext!!.getInfoAboutDevice()), {
            if (it)
                Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_backDeviceFragment)
        })

        loginBut.setOnClickListener { view ->
            if (passEt.text.isNotEmpty() && loginEt.text.isNotEmpty())
                observe(model.tryLogin(loginEt.text.toString(), passEt.text.toString())) {
                        if (!it.isNullOrEmpty()) {
                            setEmptyValues()
                            val action = LoginFragmentDirections.actionLoginFragmentToChooseTimeFragment(it)
                            Navigation.findNavController(fragment).navigate(action)
                        } else {
                            view.showSnackbar(resources.getString(R.string.invalid_pass_error_message))
                        }
                }
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
