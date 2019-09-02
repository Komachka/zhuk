package com.evo.evozhuk.notifications

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.evo.evozhuk.*
import java.util.concurrent.TimeUnit

class NotificationService : IntentService(NOTIFICATION_SERVICE_NAME) {
    companion object {
        @Volatile
        var isServiceRunning = true
    }

    override fun onHandleIntent(intent: Intent?) {

        val endTime = intent?.getLongExtra(INTENT_DATA_MILISEC, 0) ?: 0
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
        while (System.currentTimeMillis() < endTime && isServiceRunning) {
            synchronized(this) {
                try {
                    Thread.sleep(ONE_SECOND)
                    val millis = endTime - System.currentTimeMillis()
                    val hms = createFormattedDateString(millis)
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
