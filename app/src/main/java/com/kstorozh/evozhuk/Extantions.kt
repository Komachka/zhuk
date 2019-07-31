package com.kstorozh.evozhuk

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.ErrorStatus
import java.text.DecimalFormat

import android.os.StatFs
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Context.getInfoAboutDevice(): DeviceInputData {

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

    val memory = getFreeMemoryInfo()
    return DeviceInputData(Build.ID, "${Build.BRAND} ${Build.MODEL}", "android", Build.VERSION.RELEASE, this.getTotalMemoryInfo().toInt(), this.getTotalStorageInfo().toInt())
}

fun Context?.getDeviceName(): String {
    return "${Build.BRAND} ${Build.MODEL}"
}

fun Context.getInfoPairs(): List<Pair<String, String>> {

    val list = mutableListOf<Pair<String, String>>()
    list.add("VERSION" to Build.VERSION.RELEASE.toString()) // PUT to constants
    list.add("MODEL" to "${Build.BRAND} ${Build.MODEL}")
    list.add("ID" to Build.ID)
    val freeMemory = getFreeMemoryInfo()
    val totalMemory = getTotalMemoryInfo()
    val freeStorage = getFreeStorageInfo()
    val totalStorage = getTotalStorageInfo()

    val df = DecimalFormat("#.##")
    list.add("MEMORY" to "${df.format(totalMemory * 0.001)} Gb") // from Mg tu Gb

    list.add("STORAGE" to "${df.format(totalStorage * 0.001)} Gb") // from Mg tu Gb
    return list
}

private fun Context.getTotalStorageInfo(): Long {
    val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getExternalFilesDir("")
    } else {
        Environment.getExternalStorageDirectory()
    }
    file?.let {
        val stat = StatFs(it.path)
        val bytesAvailable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            stat.blockSizeLong * stat.blockCountLong
        } else {
            stat.blockSize.toLong() * stat.blockCount.toLong()
        }
        return bytesAvailable / (1024 * 1024)
    }
    return 0
}

fun Context.getFreeStorageInfo(): Long {
    val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getExternalFilesDir("")
    } else {
        Environment.getExternalStorageDirectory()
    }
    file?.let {
        val stat = StatFs(it.path)
        val bytesAvailable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            stat.blockSizeLong * stat.availableBlocksLong
        } else {
            stat.blockSize.toLong() * stat.availableBlocks.toLong()
        }
        return bytesAvailable / (1024 * 1024)
    }
    return 0
}

private fun Context?.getTotalMemoryInfo(): Long {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = this!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val availableMegs = mi.availMem / 0x100000L // MG
    val totalMegs = mi.totalMem / 0x100000L // MG
    return totalMegs
}

private fun Context?.getFreeMemoryInfo(): Long {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = this!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val availableMegs = mi.availMem / 0x100000L // MG
    val totalMegs = mi.totalMem / 0x100000L // MG
    return availableMegs
}

fun View.showSnackbar(textMessage: String, length: Int = Snackbar.LENGTH_LONG) {

    Snackbar.make(this, textMessage, length).show()
}

fun View.showErrorMessage(domainErrors: DomainErrors) {
    var message: String = resources.getString(R.string.unexpected_error_message)
    domainErrors.errorStatus?.let {
        message = when (domainErrors.errorStatus) {
            ErrorStatus.INVALID_PASSWORD -> resources.getString(R.string.invalid_pass_error_message)
            ErrorStatus.INVALID_LOGIN -> resources.getString(R.string.invalid_login_error_message)
            ErrorStatus.UNAUTHORIZED -> resources.getString(R.string.device_is_not_authorized_error_message)
            ErrorStatus.CAN_NOT_INI_DEVICE -> resources.getString(R.string.can_not_init_error_message)
            ErrorStatus.CAN_NOT_UPDATE_DEVICE -> resources.getString(R.string.can_not_update_error_message)
            ErrorStatus.CAN_NOT_BOOK_DEVICE -> resources.getString(R.string.can_not_book_error_message)
            ErrorStatus.CAN_NOT_RETURN_DEVICE -> resources.getString(R.string.can_not_return_error_message)
            ErrorStatus.CAN_NOT_GET_USERS -> resources.getString(R.string.can_not_get_users_error_message)
            ErrorStatus.CAN_NOT_REMIND_PIN -> resources.getString(R.string.can_not_remind_pin_error_message)
            else -> resources.getString(R.string.unexpected_error_message)
        }
    }
    this.showSnackbar(message)
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
    liveData.observe(this, Observer(body))
}
