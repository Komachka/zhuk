package com.kstorozh.evozhuk.returnDevice

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.findSuitableParent
import kotlinx.android.synthetic.main.view_snackbar_low_battery.view.*

class LowBatterySnackbar(parent: ViewGroup, content: LowBatterySnackbarView) :
    BaseTransientBottomBar<LowBatterySnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, R.color.round_but_color))
        getView().setPadding(0, 0, 0, 0)
        duration = LENGTH_INDEFINITE
    }

    companion object {
        fun make(view: View, mesage: String? = null): LowBatterySnackbar? {
            view.findSuitableParent()?.let { parent ->
                val customViewSnackbar = LowBatterySnackbarView(view.context)
                if (mesage != null) {
                    customViewSnackbar.message.text = mesage
                }
                return LowBatterySnackbar(
                    parent,
                    customViewSnackbar
                )
            }
            return null
        }
    }
}