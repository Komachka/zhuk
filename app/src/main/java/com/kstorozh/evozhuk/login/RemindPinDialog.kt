package com.kstorozh.evozhuk.login

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar

interface RemindPinDialog {

    fun LoginFragment.show() {
        val applicationContext = context ?: return
        val alertDialog = AlertDialog.Builder(applicationContext)
        alertDialog.setTitle(resources.getString(R.string.reset_pass_dealog_title))
        alertDialog.setMessage(resources.getString(R.string.reset_pass_dealog_message))
        val input = AutoCompleteTextView(applicationContext)
        input.setAdapter(ArrayAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, userNames.toTypedArray()))
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.reset_pass_dealog_pos_but)) { dialog, which ->
            if (input.text.isNotEmpty()) {
                observe(model.getUserByName(input.text.toString())) { user ->
                        user?.let { user ->
                            observe(model.remindPin(user)) {
                                val message: String = if (it)
                                    resources.getString(R.string.request_for_reset_success_message)
                                else
                                    resources.getString(R.string.request_for_reset_error_message)
                                view?.showSnackbar(message)
                            }
                        }
                    }
            } else {
                view?.showSnackbar(resources.getString(R.string.input_error_message))
            }
        }
        alertDialog.show()
    }
}