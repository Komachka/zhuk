package com.kstorozh.evozhuk.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

const val CHANEL_ID:String = "CHANEL_ID"
class NotificationUtil {



    companion object {
        fun createNotificationChanal(context: Context)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChanel:NotificationChannel =   NotificationChannel(CHANEL_ID, "Foreground service chanel", NotificationManager.IMPORTANCE_LOW)
                val manager:NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(serviceChanel)
            }
        }

    }
}