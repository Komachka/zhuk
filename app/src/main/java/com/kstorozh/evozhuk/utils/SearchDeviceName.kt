package com.kstorozh.evozhuk.utils

import android.os.Build
import com.jaredrummler.android.device.DeviceName

class SearchDeviceName {
    companion object {
        val name: String = DeviceName.getDeviceName()
    }
}