package com.kstorozh.evozhuk.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.lifecycle.ViewModelProviders
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.utils.getDeviceName
import com.kstorozh.evozhuk.utils.getInfoAboutDevice
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.forgotPassTv
import kotlinx.android.synthetic.main.fragment_login.view.loginEt
import kotlinx.android.synthetic.main.fragment_login.view.passwordEt
import kotlinx.android.synthetic.main.logo_and_info.view.*

class LoginFragment : Fragment(), RemindPinDialog, UserNamesDataHandler, HandleErrors {

    lateinit var model: LogInViewModel
    lateinit var userNames: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun View.setEmptyValues() {
        loginEt.setText("")
        passwordEt.setText("")
    }

    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        fragment.infoImageBut
            .setOnClickListener {
                Navigation.findNavController(fragment).navigate(LoginFragmentDirections.actionLoginFragmentToInfoFragment())
            }

        fragment.deviceNameTv.text = context?.getDeviceName()
        fragment.forgotPassTv.setOnClickListener { show() }
        model = ViewModelProviders.of(this)[LogInViewModel::class.java]
        handleErrors(model, fragment)
        subscribeNamesLiveData()
        observe(model.isDeviceBooked(context?.applicationContext!!.getInfoAboutDevice()), {
            if (it)
                Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_backDeviceFragment)
        })
        fragment.goInBut.setOnClickListener { view ->
            if (fragment.passwordEt.text!!.isNotEmpty() && loginEt.text.isNotEmpty())
                observe(model.tryLogin(loginEt.text.toString(), passwordEt.text.toString())) {
                    if (it != USER_ID_NOT_SET) {
                        fragment.setEmptyValues()
                        val action = LoginFragmentDirections.actionLoginFragmentToChooseTimeFragment(it)
                        Navigation.findNavController(fragment).navigate(action)
                    }
                }
            else
                view.showSnackbar(resources.getString(R.string.pass_is_empty_error_message))
        }
    }
}
