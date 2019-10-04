package com.evo.evozhuk.login

import com.evo.domainapi.model.NearbyDomainBooking
import com.evo.evozhuk.R
import com.evo.evozhuk.returnDevice.LowBatterySnackbar
import kotlinx.android.synthetic.main.fragment_login.*
import org.joda.time.DateTime

interface NearbyBookingInfo {
    fun LoginFragment.showBookingInfo(it: NearbyDomainBooking) {
        val start = DateTime(it.startDate)
        if (start.dayOfMonth == DateTime().dayOfMonth) {
            val minute = if (start.minuteOfHour > 9) "${start.minuteOfHour}" else "0" + start.minuteOfHour
            val message =
                "${resources.getString(R.string.nearby_booking_message)} ${start.hourOfDay}:$minute @${it.userName}"
            LowBatterySnackbar.make(this.snackbarlocationLogin, message)?.show()
        }
    }
}