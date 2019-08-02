package com.kstorozh.evozhuk.login

import android.R
import android.widget.ArrayAdapter
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_login.*

interface UserNamesDataHandler {

    fun LoginFragment.subscribeNamesLiveData() {
        observe(model.getUserNames()) {
            userNames = it
            this.loginEt.setAdapter(ArrayAdapter(context!!, R.layout.simple_dropdown_item_1line, it.toTypedArray()))
        }
    }
}