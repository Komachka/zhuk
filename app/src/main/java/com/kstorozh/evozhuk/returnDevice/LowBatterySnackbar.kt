package com.kstorozh.evozhuk.returnDevice

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.findSuitableParent

class LowBatterySnackbar(parent: ViewGroup, content: LowBatterySnackbarView) :
    BaseTransientBottomBar<LowBatterySnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, R.color.round_but_color))
        getView().setPadding(0, 0, 0, 0)
        duration = LENGTH_LONG
    }

    companion object {
        fun make(view: View): LowBatterySnackbar {
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )
            val customViewSnackbar = LowBatterySnackbarView(view.context)
            return LowBatterySnackbar(
                parent,
                customViewSnackbar
            )
        }
    }
}