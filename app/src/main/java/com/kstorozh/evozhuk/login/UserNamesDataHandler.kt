package com.kstorozh.evozhuk.login

import android.R
import android.widget.ArrayAdapter
import com.kstorozh.evozhuk.observe

interface UserNamesDataHandler {

    fun LoginFragment.subscribeNamesLiveData() {
        observe(model.getUserNames()) {
            userNames = it
            loginEt.setAdapter(ArrayAdapter(context!!, R.layout.simple_dropdown_item_1line, it.toTypedArray()))
        }
    }
}