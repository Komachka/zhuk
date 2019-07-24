package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.domainapi.model.SessionData
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.chooseTime.ChooseTimeSharedViewModel
import java.text.SimpleDateFormat
import android.app.AlarmManager
import android.app.NotificationManager
import android.os.SystemClock
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kstorozh.evozhuk.MainActivity
import com.kstorozh.evozhuk.backDevice.MyNotificationPublisher.Companion.CHANNEL_ID
import com.kstorozh.evozhuk.notifications.INTENT_DATA_MILISEC
import com.kstorozh.evozhuk.notifications.NotificationService
import java.util.*



class BackDeviceFragment : Fragment() {

    lateinit var modelBackDevice: BackDeviceViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_back_device, container, false)
        val textView: TextView = view.findViewById(R.id.dateToBack)
        val button: Button = view.findViewById(R.id.giveBackBut)

        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)
        modelBackDevice = ViewModelProviders.of(activity!!).get(BackDeviceViewModel::class.java)
        modelBackDevice.getSessionData().observe(this, Observer {
            it?.let {
                val format = SimpleDateFormat("hh:mm\ndd MMMM") // TODO move it from here
                textView.setText(format.format(it.endData.time))
                //scheduleNotification(context!!, 1000, it.endData)
            }
        })

        modelChooseTime.choosenData.value?.let {
            val endDate = modelChooseTime.choosenData.value
            val userId = modelChooseTime.userId.value
            modelBackDevice.setBookingSession(SessionData(userId!!, endDate!!))
            scheduleNotification(context!!, 20000, endDate)
            startForegroundService(endDate)
        }

        button.setOnClickListener {

            modelBackDevice.tryReturnDevice().observe(this, Observer {
                Toast.makeText(context, "is device returned $it", Toast.LENGTH_LONG).show()
                if (it) {
                    clearAllNotification(context)
                    stopForegroundService()
                    Navigation.findNavController(view).navigate(R.id.action_backDeviceFragment_to_loginFragment)
                }
            })
        }

        return view
    }

    private fun clearAllNotification(context: Context?) {
        val notificationManager:NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun scheduleNotification(context: Context, delay: Long, endTime: Calendar) { // delay is after how much time(in millis) from current time you want to schedule the notification

        val notificationId = 1

        val format = SimpleDateFormat("hh:mm dd MMMM")
        val color = ContextCompat.getColor(context, R.color.background)
        val builder = NotificationCompat.Builder(context)
            .setContentTitle("You need to back device")
            .setContentText("in ${format.format(endTime.time)}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setChannelId(CHANNEL_ID)
            .setColor(color) //TODO does not work

        val intent = Intent(context, MainActivity::class.java)
        val activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(activity)

        val notification = builder.build()

        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent =
            PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)

         val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }


    private fun startForegroundService(calendar: Calendar) {
        val serviceIntent = Intent(context, NotificationService::class.java)
        serviceIntent.putExtra(INTENT_DATA_MILISEC, calendar.time.time)
        context!!.startService(serviceIntent)
    }


    private fun stopForegroundService()
    {
        val serviceIntent = Intent(context, NotificationService::class.java)
        context!!.stopService(serviceIntent)
    }
}