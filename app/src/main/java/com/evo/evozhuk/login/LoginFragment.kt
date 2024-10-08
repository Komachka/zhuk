package com.evo.evozhuk.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.evo.evozhuk.*
import com.evo.evozhuk.utils.getDeviceName
import com.evo.evozhuk.utils.getInfoAboutDevice
import com.evo.evozhuk.utils.observe
import com.evo.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.forgotPassTv
import kotlinx.android.synthetic.main.fragment_login.view.loginEt
import kotlinx.android.synthetic.main.fragment_login.view.passwordEt
import kotlinx.android.synthetic.main.logo_and_info.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), RemindPinDialog, UserNamesDataHandler, HandleErrors,
    NearbyBookingInfo {

    val model: LogInViewModel by viewModel()
    val userNames = ArrayList<String>()

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
                Navigation.findNavController(fragment)
                    .navigate(LoginFragmentDirections.actionLoginFragmentToInfoFragment())
            }

        fragment.deviceNameTv.text = context?.getDeviceName()
        fragment.forgotPassTv.setOnClickListener { show() }
        fragment.calendarImageBut.visibility = View.INVISIBLE
        viewLifecycleOwner.handleErrors(model, fragment)
        subscribeNamesLiveData()
        viewLifecycleOwner.observe(
            model.isDeviceBooked(context?.applicationContext!!.getInfoAboutDevice()),
            {
                if (it)
                    Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_backDeviceFragment)
            })
        viewLifecycleOwner.observe(model.getNearbyBooking()) {
            showBookingInfo(it)
        }
        fragment.goInBut.setOnClickListener { view ->
            if (fragment.passwordEt.text!!.isNotEmpty() && loginEt.text.isNotEmpty())
                viewLifecycleOwner.observe(
                    model.tryLogin(
                        loginEt.text.toString(),
                        passwordEt.text.toString()
                    )
                ) {
                    if (it != USER_ID_NOT_SET) {
                        fragment.setEmptyValues()
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToBookOrTakeFragment(it)
                        Navigation.findNavController(fragment).navigate(action)
                    }
                }
            else
                view.showSnackbar(resources.getString(R.string.pass_is_empty_error_message))
        }
    }
}
