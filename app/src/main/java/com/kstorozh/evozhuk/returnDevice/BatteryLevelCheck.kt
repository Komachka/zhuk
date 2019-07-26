package com.kstorozh.evozhuk.returnDevice

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
import android.widget.TextView
import com.kstorozh.evozhuk.R

interface BatteryLevelCheck {

    fun View.manageBatteryCharge() {
        val needToCharge = findViewById<TextView>(R.id.needToChargeTv)
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context?.registerReceiver(null, ifilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        if (level!! < BATTERY_LEVEL_TO_CHARGE)
            needToCharge.text = "${resources.getString(R.string.chargeDeviceMessage)}"
    }
}