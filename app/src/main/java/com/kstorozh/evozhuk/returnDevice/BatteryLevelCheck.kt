package com.kstorozh.evozhuk.returnDevice

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
import com.kstorozh.evozhuk.BATTERY_LEVEL_TO_CHARGE
import kotlinx.android.synthetic.main.fragment_return_device.view.*

interface BatteryLevelCheck {

    fun View.manageBatteryCharge() {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context?.registerReceiver(null, ifilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        if (level!! < BATTERY_LEVEL_TO_CHARGE)
            LowBatterySnackbar.make(this.snackbarlocation)?.show()
    }
}