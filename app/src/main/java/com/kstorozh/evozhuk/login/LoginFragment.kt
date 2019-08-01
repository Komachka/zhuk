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

class LoginFragment : Fragment(), RemindPinDialog, UserNamesDataHandler {

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
        subscribeNamesLiveData()

        observe(model.errorLiveData) {
            var message: String = it.toString()
            it.message?.let { message = it }
            if (it.message == null) it.throwable?.message?.let { message = it }
            activity?.currentFocus?.showSnackbar(message)
        }

        observe(model.isDeviceBooked(context?.applicationContext!!.getInfoAboutDevice()), {
            if (it)
                Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_backDeviceFragment)
        })

        fragment.goInBut.setOnClickListener { view ->
            if (fragment.passwordEt.text!!.isNotEmpty() && loginEt.text.isNotEmpty())
                observe(model.tryLogin(loginEt.text.toString(), passwordEt.text.toString())) {
                    if (it != USER_ID_NOT_SET) {
                        fragment.setEmptyValues()
                    } else {
                        fragment.showSnackbar(resources.getString(R.string.invalid_pass_error_message))
                    }
                }
            else
                view.showSnackbar(resources.getString(R.string.pass_is_empty_error_message))
        }
        observe(model.tryLoginLiveData) { // Navigation crashed in onclick
            if (it != USER_ID_NOT_SET) {
                fragment.setEmptyValues()
                val action = LoginFragmentDirections.actionLoginFragmentToChooseTimeFragment(it)
                Navigation.findNavController(fragment).navigate(action)
                model.tryLoginLiveData.value = USER_ID_NOT_SET
            }
        }
    }
}
