package com.kstorozh.evozhuk.login

import com.kstorozh.domainapi.model.NearbyDomainBooking
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.returnDevice.LowBatterySnackbar
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