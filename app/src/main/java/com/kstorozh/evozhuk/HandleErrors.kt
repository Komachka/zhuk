package com.kstorozh.evozhuk

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar

interface HandleErrors {

    fun LifecycleOwner.handleErrors(viewModel: BaseViewModel, view: View) {
        observe(viewModel.errors) {
            it.message?.let { view.showSnackbar(it) }
            if (it.message == null) {
                it.throwable?.message?.let {
                    view.showSnackbar(it)
                }
            }
        }
    }
}