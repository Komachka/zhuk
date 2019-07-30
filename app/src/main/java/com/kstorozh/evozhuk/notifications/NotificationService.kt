package com.kstorozh.evozhuk.notifications

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kstorozh.evozhuk.*
import java.util.concurrent.TimeUnit

class NotificationService : IntentService("Hello intent service") {

    var STOP_SERVICE_FLAG = "SERVICE_RUNNING"

    override fun onHandleIntent(intent: Intent?) {

        Log.d(LOG_TAG, "on handle service")
        val endTime = intent!!.getLongExtra(INTENT_DATA_MILISEC, 0)
        val stopFlag = intent!!.getStringExtra(INTENT_STOP_FLAG)
        if (stopFlag != null && stopFlag == INTENT_STOP_FLAG) {
            STOP_SERVICE_FLAG = stopFlag
        }
        val notificationId = 2
        val intent = Intent(this, MainActivity::class.java)
        val activity = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val hms = createFormattedDateString(endTime)
        val builder = createBuilderNotification(hms)
        builder.setContentIntent(activity)
        startForeground(notificationId, builder.build())
        updateNotificationInLoop(endTime, builder, notificationId)
    }

    private fun createBuilderNotification(hms: String): NotificationCompat.Builder {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val builder = NotificationCompat.Builder(this, CHANEL_ID)
                .setContentTitle(resources.getString(R.string.you_need_to_back_device_in_next_notif_message))
                .setContentText(hms)
                .setSmallIcon(R.drawable.ic_timer_black_24dp)
                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOngoing(false)
                .setStyle(NotificationCompat.BigTextStyle().bigText(hms)
                    .setBigContentTitle(resources.getString(R.string.you_need_to_back_device_in_next_notif_message))
                )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder.color = Color.RED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.priority = NotificationCompat.PRIORITY_LOW
        }

        return builder
    }

    private fun updateNotificationInLoop(
        endTime: Long,
        builder: NotificationCompat.Builder,
        notificationId: Int
    ) {
        while (System.currentTimeMillis() < endTime && STOP_SERVICE_FLAG != INTENT_STOP_FLAG) {
            synchronized(this) {
                try {
                    Thread.sleep(1000)
                    val millis = endTime - System.currentTimeMillis()
                    val hms = createFormattedDateString(millis)
                    // Log.d(LOG_TAG, millis.toString())
                    builder.setContentText(hms)
                    builder.setStyle(NotificationCompat.BigTextStyle().bigText(hms).setBigContentTitle(resources.getString(R.string.you_need_to_back_device_in_next_notif_message)))
                    startForeground(notificationId, builder.build())
                } catch (e: Exception) {
                    Log.e(LOG_TAG, e.message!!)
                }
            }
        }
    }

    private fun createFormattedDateString(millis: Long): String {
        return String.format(
            DATE_FORMAT_TIMER, TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millis
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "on create service")
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "on destroy service")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(LOG_TAG, "on bing")
        TODO("Return the communication channel to the service.")
    }
}
