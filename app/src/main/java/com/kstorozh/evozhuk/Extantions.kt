package com.kstorozh.evozhuk

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.ErrorStatus

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

fun Context?.getInfoPairs(): List<Pair<String, String>> {

    val list = mutableListOf<Pair<String, String>>()
    list.add("VERSION" to Build.VERSION.SDK_INT.toString()) // PUT to constants
    list.add("MODEL" to "${Build.BRAND} ${Build.MODEL}")
    list.add("ID" to Build.ID)
    val memory = getMemoryInfo()
    list.add("MEMORY" to "${memory.first * 0.001}") // from Mg tu Gb
    list.add("STORAGE" to "${memory.second * 0.001}") // from Mg tu Gb
    return list
}

private fun Context?.getMemoryInfo(): Pair<Long, Long> {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = this!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val availableMegs = mi.availMem / 0x100000L // MG
    val totalMegs = mi.totalMem / 0x100000L // MG
    return availableMegs to totalMegs
}

fun View.showSnackbar(textMessage: String, length: Int = Snackbar.LENGTH_LONG) {

    Snackbar.make(this, textMessage, length).show()
}

fun View.showErrorMessage(domainErrors: DomainErrors) {
    if (domainErrors.message.isNullOrEmpty()) {
        val message = when (domainErrors.errorStatus) {
            ErrorStatus.INVALID_PASSWORD -> resources.getString(R.string.invalid_pass_error_message)
            ErrorStatus.INVALID_LOGIN -> resources.getString(R.string.invalid_login_error_message)
            ErrorStatus.UNAUTHORIZED -> resources.getString(R.string.device_is_not_authorized_error_message)
            ErrorStatus.CAN_NOT_INI_DEVICE -> resources.getString(R.string.can_not_init_error_message)
            ErrorStatus.CAN_NOT_UPDATE_DEVICE -> resources.getString(R.string.can_not_update_error_message)
            ErrorStatus.CAN_NOT_BOOK_DEVICE -> resources.getString(R.string.can_not_book_error_message)
            ErrorStatus.CAN_NOT_RETURN_DEVICE -> resources.getString(R.string.can_not_return_error_message)
            ErrorStatus.CAN_NOT_GET_USERS -> resources.getString(R.string.can_not_get_users_error_message)
            ErrorStatus.CAN_NOT_REMIND_PIN -> resources.getString(R.string.can_not_remind_pin_error_message)
            else -> resources.getString(R.string.unexpected_error_message) + " " + domainErrors.throwable.message
        }
        this.showSnackbar(message)
        Log.d(LOG_TAG, "Message $message")
    } else {
        this.showSnackbar(domainErrors.message)
        Log.d(LOG_TAG, "Domain message ${domainErrors.message}")
    }
}
