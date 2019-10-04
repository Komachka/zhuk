package com.evo.evozhuk

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.evo.evozhuk.utils.observe
import com.evo.evozhuk.utils.showSnackbar

interface HandleErrors {

    fun LifecycleOwner.handleErrors(viewModel: BaseViewModel, view: View) {
        observe(viewModel.errors) {
            it.getContentIfNotHandled()?.let {
                it.message?.let { view.showSnackbar(it) }
                if (it.message == null) {
                    it.throwable?.message?.let {
                        view.showSnackbar(it)
                    }
                }
            }
        }
    }
}