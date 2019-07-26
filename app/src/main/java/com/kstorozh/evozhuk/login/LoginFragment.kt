package com.kstorozh.evozhuk.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R
import androidx.lifecycle.ViewModelProviders
import android.widget.LinearLayout
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.kstorozh.evozhuk.getDeviceName
import com.kstorozh.evozhuk.getInfoAboutDevice

import com.kstorozh.evozhuk.showSnackbar

class LoginFragment : Fragment() {

    // TODO move it inside method

    lateinit var model: LogInViewModel
    lateinit var loginBut: Button
    lateinit var loginEt: AutoCompleteTextView
    lateinit var passEt: EditText
    lateinit var forgotPassTv: TextView

    private lateinit var userNames: ArrayList<String>

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

        //val info = context.getInfoAboutDevice()
        fragment.findViewById<TextView>(R.id.deviceNameTv).text = context.getDeviceName()

        loginBut = fragment.findViewById(R.id.goInBut)
        loginEt = fragment.findViewById(R.id.loginEt) as AutoCompleteTextView
        passEt = fragment.findViewById(R.id.passwordEt) as EditText
        forgotPassTv = fragment.findViewById(R.id.forgotPassTv)
        forgotPassTv.setOnClickListener {
            showRemindPinDialog()
        }
        model = ViewModelProviders.of(this)[LogInViewModel::class.java]
        model.getUserNames().observe(this, Observer {
            userNames = it
            loginEt.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, it.toTypedArray()))
        })
        loginBut.setOnClickListener { view ->

            if (passEt.text.isNotEmpty() && loginEt.text.isNotEmpty())
                    model.tryLogin(loginEt.text.toString(), passEt.text.toString()).observe(this, Observer {
                        if (!it.isNullOrEmpty()) {
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

    private fun showRemindPinDialog() {
        val alertDialog = AlertDialog.Builder(context!!)

        alertDialog.setTitle(resources.getString(R.string.reset_pass_dealog_title))
        alertDialog.setMessage(resources.getString(R.string.reset_pass_dealog_message))

        val input = AutoCompleteTextView(context)
        input.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, userNames.toTypedArray()))

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton(resources.getString(R.string.reset_pass_dealog_pos_but)) { dialog, which ->
            if (input.text.isNotEmpty()) {
                model.getUserByName(input.text.toString()).observe(this, Observer {
                    it?.let {
                        model.remindPin(it).observe(
                            this, Observer {
                                var message: String
                                if (it)
                                    message = resources.getString(R.string.request_for_reset_success_message)
                                else
                                    message = resources.getString(R.string.request_for_reset_error_message)
                                view!!.showSnackbar(message)
                            }
                        )
                    }
                })
            } else {
                view!!.showSnackbar(resources.getString(R.string.input_error_message))
            }
        }
        alertDialog.show()
    }
}
