package com.kstorozh.evozhuk.login

import android.os.Bundle
import android.util.Log
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

class LoginFragment : Fragment() {

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
        loginBut = fragment.findViewById(R.id.goInBut)

        loginEt = fragment.findViewById(R.id.loginEt) as AutoCompleteTextView
        passEt = fragment.findViewById(R.id.passwordEt) as EditText

        forgotPassTv = fragment.findViewById(R.id.forgotPassTv)

        forgotPassTv.setOnClickListener {
            showDialog()
        }

        model = ViewModelProviders.of(activity!!).get(LogInViewModel::class.java)

        model.getUserNames().observe(this, Observer {
            Log.d("MainActivity", it.toString())
            userNames = it
            loginEt.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, it.toTypedArray()))
        })

        // loginEt.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, userNames))

        loginBut.setOnClickListener {

            if (passEt.text.isNotEmpty() && loginEt.text.isNotEmpty())
                // TODO delete this observation
                model.tryLogin(loginEt.text.toString(), passEt.text.toString()).observe(this, Observer {
                    // Toast.makeText(context, it ?: "Did not login", Toast.LENGTH_LONG).show()
                })
            /*else if (!userNames.contains<String>(loginEt.text.toString().toLowerCase()))
                Toast.makeText(context, "This user is not consists in users list", Toast.LENGTH_LONG).show()*/
            else
                Toast.makeText(context, "The pass is empty", Toast.LENGTH_LONG).show()
        }

        model.userIdLiveData.observe(this, Observer {
            if (!it.isNullOrEmpty())
                Navigation.findNavController(fragment).navigate(R.id.action_loginFragment_to_chooseTimeFragment)
            else {
                Toast.makeText(context, "Can not login. Invalid password", Toast.LENGTH_LONG).show()
            }
        })

        return fragment
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
        alertDialog.setTitle("Reset pass")
        alertDialog.setMessage("Enter slack login")

        val input = AutoCompleteTextView(context)
        input.setAdapter(ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, userNames.toTypedArray()))

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton("Send") { dialog, which ->
            Toast.makeText(context, "Request for reset pin was not sent", Toast.LENGTH_LONG).show()
            if (input.text.isNotEmpty()) {
                model.getUserByName(input.text.toString()).observe(this, Observer {
                    it?.let {
                        Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                        model.remindPin(it).observe(
                            this, Observer {
                                if (it)
                                    Toast.makeText(context, "Request for reset pin was sent", Toast.LENGTH_LONG).show()
                                else
                                    Toast.makeText(context, "Request for reset pin was not sent", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                })
            } else {
                Toast.makeText(context, "Incorrect input", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.show()
    }
}
