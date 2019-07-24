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
import com.kstorozh.evozhuk.MainActivity
import java.util.concurrent.TimeUnit
import com.kstorozh.evozhuk.R


const val INTENT_DATA_MILISEC = "INTENT_DATA_MILISEC"
const val DATE_FORMAT = "%02d:%02d:%02d"
const val LOG_TAG: String = "MainActivity"

class NotificationService : IntentService("Hello intent service") {

    override fun onHandleIntent(intent: Intent?) {

        Log.d(LOG_TAG, "on handle service")
        val endTime = intent!!.getLongExtra(INTENT_DATA_MILISEC, 0)
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
                .setContentTitle("You need to back device in next")
                .setContentText(hms)
                .setSmallIcon(R.drawable.ic_timer_black_24dp)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOngoing(false)
                .setStyle(NotificationCompat.BigTextStyle().bigText(hms)
                    .setBigContentTitle("You need to return device in next")
                    .setSummaryText("Summary text"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder.setColor(Color.RED)
                .setChannelId(CHANEL_ID)

        return builder
    }

    private fun updateNotificationInLoop(endTime: Long, builder:NotificationCompat.Builder, notificationId:Int) {
        while (System.currentTimeMillis() < endTime) {
            synchronized(this) {
                try {
                    Log.d(LOG_TAG, "in try $endTime ${System.currentTimeMillis()} ${System.currentTimeMillis() - endTime} ")
                    Thread.sleep(1000)
                    val millis = endTime - System.currentTimeMillis()
                    val hms = createFormattedDateString(millis)
                    builder.setContentText(hms)
                    builder.setStyle(NotificationCompat.BigTextStyle().bigText(hms).setBigContentTitle("You need to return device").setSummaryText("Summary text"))
                    startForeground(notificationId, builder.build())

                }
                catch (e: Exception) {
                    Log.e(LOG_TAG, e.message!!)
                }

            }
        }
    }

    private fun createFormattedDateString(millis: Long): String {
        return String.format(
            DATE_FORMAT, TimeUnit.MILLISECONDS.toHours(millis),
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
