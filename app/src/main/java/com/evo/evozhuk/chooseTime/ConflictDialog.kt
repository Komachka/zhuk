package com.evo.evozhuk.chooseTime

import androidx.appcompat.app.AlertDialog
import com.evo.domainapi.model.DomainErrorData
import com.evo.evozhuk.DATE_FORMAT_NOTIFICATION_MESSAGE2
import com.evo.evozhuk.R
import java.text.SimpleDateFormat

interface ConflictDialog {

    fun ChooseTimeFragment.showFixConflictDialog(data: DomainErrorData, takeDevice: () -> Unit, calendarNavigate: () -> Unit) {

        val applicationContext = context ?: return
        val alertDialog = AlertDialog.Builder(applicationContext)
        alertDialog.setTitle(resources.getString(R.string.conflict_title))
        val formatter = SimpleDateFormat(DATE_FORMAT_NOTIFICATION_MESSAGE2)
        val message = "${resources.getString(R.string.this_booking_is_reserved)} @${data.username}" +
                " ${resources.getString(R.string.this_booking_is_reserved_from)} ${formatter.format(data.start)} " +
                "${resources.getString(R.string.this_booking_is_reserved_to)} ${formatter.format(data.end)}."
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(resources.getString(R.string.take_device_now)) { dialog, which ->
            takeDevice.invoke()
        }
        alertDialog.setNegativeButton(resources.getString(R.string.dont_take_a_device)) { dialog, which ->
            calendarNavigate.invoke()
        }
        alertDialog.show()
    }
}