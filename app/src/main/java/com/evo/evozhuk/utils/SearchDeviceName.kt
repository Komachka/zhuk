package com.evo.evozhuk.utils

import com.jaredrummler.android.device.DeviceName

class SearchDeviceName {
    companion object {
        val name: String = DeviceName.getDeviceName()
    }
}