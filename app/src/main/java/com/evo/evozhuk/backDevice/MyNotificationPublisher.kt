package com.evo.evozhuk.backDevice

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.os.Build
import com.evo.evozhuk.R
import com.evo.evozhuk.notifications.CHANEL_ID

class MyNotificationPublisher : BroadcastReceiver() {

    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = intent!!.getParcelableExtra<Notification>(NOTIFICATION)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        val name = context.getResources().getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        notification?.let {
            notificationManager.notify(notificationId, notification)
        }
    }
}