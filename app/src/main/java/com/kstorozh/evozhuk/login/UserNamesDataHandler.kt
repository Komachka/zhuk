package com.kstorozh.evozhuk.login

import android.widget.ArrayAdapter
import android.R
import com.kstorozh.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_login.*

interface UserNamesDataHandler {

    fun LoginFragment.subscribeNamesLiveData() {
        viewLifecycleOwner.observe(model.getUserNames()) { list ->
            userNames.addAll(list)
            context?.let {
                this.loginEt.setAdapter(ArrayAdapter(it, R.layout.simple_dropdown_item_1line, list.toTypedArray()))
            }
        }
    }
}