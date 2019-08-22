package com.kstorozh.evozhuk.chooseTime

import androidx.appcompat.app.AlertDialog
import com.kstorozh.evozhuk.R

interface ConflictDialog {

    fun ChooseTimeFragment.showFixConflictDialog(takeDevice: () -> Unit) {

        val applicationContext = context ?: return
        val alertDialog = AlertDialog.Builder(applicationContext)
        alertDialog.setTitle(resources.getString(R.string.conflict_title))
        alertDialog.setMessage(resources.getString(R.string.this_booking_is_reserved))
        alertDialog.setPositiveButton(resources.getString(R.string.take_device_now)) { dialog, which ->
            takeDevice.invoke()
        }
        alertDialog.setNegativeButton(resources.getString(R.string.dont_take_a_device)) { dialog, which ->
        }
        alertDialog.show()
    }
}