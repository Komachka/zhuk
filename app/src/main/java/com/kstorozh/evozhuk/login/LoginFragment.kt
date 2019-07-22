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
import android.widget.LinearLayout
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.kstorozh.domainapi.model.User

class LoginFragment : Fragment() {

    lateinit var model: LogInViewModel
    lateinit var loginBut: Button
    lateinit var loginEt: AutoCompleteTextView
    lateinit var passEt: EditText
    lateinit var forgotPassTv: TextView
    private val userNames = arrayOf("storozhkateryna", "thesickboii", "dmitriy.senchik", "katya", "petro", "vasa", "boris", "natasha", "ibragim", "qwerty", "qwertyqwe", "qwerty67868")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment: View = inflater.inflate(R.layout.fragment_login, container, false)
        loginBut = fragment.findViewById(R.id.goInBut)

        loginEt = fragment.findViewById(R.id.loginEt) as AutoCompleteTextView
        passEt = fragment.findViewById(R.id.passwordEt) as EditText

        forgotPassTv = fragment.findViewById(R.id.forgotPassTv)

        forgotPassTv.setOnClickListener {
            showDialog()
        }

        /*model.getUsers().observe(this, Observer {
            mAutoCompleteTextView.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, it))
        })*/

        model = ViewModelProviders.of(activity!!).get(LogInViewModel::class.java)

        loginEt.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, userNames))

        loginBut.setOnClickListener {

            if (userNames.contains<String>(loginEt.text.toString().toLowerCase()) && passEt.text.isNotEmpty())

                model.tryLogin(loginEt.text.toString(), passEt.text.toString()).observe(this, Observer {
                    Toast.makeText(context, it ?: "Does not login", Toast.LENGTH_LONG).show()
                })
            else
                Toast.makeText(context, "Wrong loginEt", Toast.LENGTH_LONG).show()
        }

        model.userIdLiveData.observe(this, Observer {
            if (!it.isNullOrEmpty())
                Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_chooseTimeFragment)
        })

        return fragment
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
        alertDialog.setTitle("Reset pass")
        alertDialog.setMessage("Enter slack login")

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton("Send") { dialog, which ->
            if (input.text.isNotEmpty() && userNames.contains<String>(input.text.toString().toLowerCase())) {
                model.remindPin(User(5, "ULHRKV56C", "storozhkateryna")).observe(
                    this, Observer {
                        if (it)
                            Toast.makeText(context, "Request for reset pin was sent", Toast.LENGTH_LONG).show()
                        else
                            Toast.makeText(context, "Request for reset pin was not sent", Toast.LENGTH_LONG).show()
                    }
                )
            } else {
                Toast.makeText(context, "Incorrect input", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.show()
    }
}
