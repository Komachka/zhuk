package com.kstorozh.evozhuk.utils

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
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kstorozh.evozhuk.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal fun Context.getInfoAboutDevice(): DeviceInputData {
    return DeviceInputData(
        Build.ID,
        getDeviceName(),
        OS, Build.VERSION.RELEASE,
        getTotalMemoryInfoInBite().mgToGb().toInt(),
        getTotalStorageInfoInBite().mgToGb().toInt())
}

internal fun Context.getDeviceName(): String {
    return SearchDeviceName.name
}

internal fun Context.getInfoPairs(): List<Pair<DeviceInfoName, DeviceInfoParam>> {
    val df = DecimalFormat(MEMORY_DECIMAL_FORMAT)
    return listOf(
        INFO_VERSION to Build.VERSION.RELEASE.toString(),
        INFO_MODEL to getDeviceName(),
        INFO_ID to Build.ID,
        INFO_MEMORY to "${df.format(getTotalMemoryInfoInBite().biteToGb())} Gb",
        INFO_STORAGE to "${df.format(getTotalStorageInfoInBite().biteToGb())} Gb"
    )
}

private fun Context.getTotalStorageInfoInBite(): Long {
    getStatFs()?.let {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            it.blockSizeLong * it.blockCountLong
        } else {
            it.blockSize.toLong() * it.blockCount.toLong()
        }
    }
    return 0
}

fun Context.getFreeStorageInfoInBite(): Long {
    getStatFs()?.let {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            it.blockSizeLong * it.availableBlocksLong
        } else {
            it.blockSize.toLong() * it.availableBlocks.toLong()
        }
    }
    return 0
}

fun Context.getStatFs(): StatFs? {
    Log.d(LOG_TAG, Build.VERSION.SDK_INT.toString())
    val file = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        getExternalFilesDir("")
    } else {
        Environment.getExternalStorageDirectory()
    }
    var statf: StatFs? = null
    GlobalScope.launch {
        if (file != null) {
            statf = StatFs(file.path)
        }
    }
    return statf
}

private fun Context.getFreeMemoryInfoInBite() = getMemoryInfo().availMem
private fun Context.getTotalMemoryInfoInBite() = getMemoryInfo().totalMem

private fun Context.getMemoryInfo(): ActivityManager.MemoryInfo {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    return mi
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

private fun Long.mgToGb() = this * 0.001
private fun Long.biteToMg() = this / 0X100000 // 1024 * 1024
private fun Long.biteToGb() = biteToMg().mgToGb()
