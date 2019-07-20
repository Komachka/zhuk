package com.kstorozh.evozhuk

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.kstorozh.domainapi.model.DeviceInputData

fun Context?.getInfoAboutDevice(): DeviceInputData {

    val details = ("VERSION.RELEASE : " + Build.VERSION.RELEASE +
            "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL +
            "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT +
            "\nBOARD : " + Build.BOARD +
            "\nBOOTLOADER : " + Build.BOOTLOADER +
            "\nBRAND : " + Build.BRAND +
            "\nCPU_ABI : " + Build.CPU_ABI +
            "\nCPU_ABI2 : " + Build.CPU_ABI2 +
            "\nDISPLAY : " + Build.DISPLAY +
            "\nFINGERPRINT : " + Build.FINGERPRINT +
            "\nHARDWARE : " + Build.HARDWARE +
            "\nHOST : " + Build.HOST +
            "\nID : " + Build.ID +
            "\nMANUFACTURER : " + Build.MANUFACTURER +
            "\nMODEL : " + Build.MODEL +
            "\nPRODUCT : " + Build.PRODUCT +
            "\nSERIAL : " + Build.SERIAL +
            "\nTAGS : " + Build.TAGS +
            "\nTIME : " + Build.TIME +
            "\nTYPE : " + Build.TYPE +
            "\nUNKNOWN : " + Build.UNKNOWN +
            "\nUSER : " + Build.USER)

    val memory = getMemoryInfo()
    return DeviceInputData(Build.ID, "${Build.BRAND} ${Build.MODEL}", "android", Build.VERSION.SDK_INT.toString(), memory.first.toInt(), memory.second.toInt())
}

private fun Context?.getMemoryInfo(): Pair<Long, Long> {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = this!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val availableMegs = mi.availMem / 0x100000L
    val totalMegs = mi.totalMem / 0x100000L
    return availableMegs to totalMegs
}
