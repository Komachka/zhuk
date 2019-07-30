package com.kstorozh.evozhuk.chooseTime

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.backDevice.MyNotificationPublisher
import com.kstorozh.evozhuk.notifications.CHANEL_ID
import com.kstorozh.evozhuk.notifications.NotificationService
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.fragment_time_choose.view.*
import kotlinx.android.synthetic.main.logo_and_info.view.*

class ChooseTimeFragment : Fragment() {

    var selectedButton: Button? = null
        private set(value) {
            field = value
            value!!.setBackgroundResource(R.drawable.time_but_pressed)
            value.setTextColor(resources.getColor(R.color.but_time_def))
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_time_choose, container, false)
        view.infoImageBut.setOnClickListener {
                Navigation.findNavController(view).navigate(ChooseTimeFragmentDirections.actionChooseTimeFragmentToInfoFragment())
            }

        view.deviceNameTv.text = context.getDeviceName()
        view.youTakeDeviceLabelTv.text =
            "${resources.getString(R.string.time_choose_label)}${context.getDeviceName()}?"

        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeSharedViewModel::class.java)
        var milisec = ChooseTimeFragmentArgs.fromBundle(arguments!!).milisec
        if (milisec == 0L)
            milisec = TimeUtils.setHours(1)

        val userId = ChooseTimeFragmentArgs.fromBundle(arguments!!).userId
        if (userId != USER_ID_NOT_SET)
            modelChooseTime.setUserId(userId)
        modelChooseTime.setCalendar(milisec)

        val buttonList = listOf<Button>(
            view.oneHourBut.also {
                selectedButton = it
            },
            view.twoHourBut,
            view.fourHourBut,
            view.allDayBut,
            view.twoDaysBut,
            (view.anotherTimeBut as Button).also {
                if (ChooseTimeFragmentArgs.fromBundle(arguments!!).milisec != 0L) {
                    selectedButton?.let { it1 -> resetButton(it1) }
                    selectedButton = it
                }
            }
        )

        buttonList.forEach { button ->
            button.setOnClickListener {
                (it as Button).setBackgroundResource(R.drawable.time_but_pressed)
                it.setTextColor(resources.getColor(R.color.but_time_focus))
                selectedButton?.let {
                    resetButton(it)
                }
                selectedButton = it
                milisec = when (it.id) {
                        R.id.oneHourBut -> TimeUtils.setHours(1)
                        R.id.twoHourBut -> TimeUtils.setHours(2)
                        R.id.fourHourBut -> TimeUtils.setHours(4)
                        R.id.twoDaysBut -> TimeUtils.setHours(48)
                        R.id.allDayBut ->
                        {
                            val currentTime = GregorianCalendar.getInstance()
                            val mCalendar = GregorianCalendar(currentTime.get(Calendar.YEAR),
                                currentTime.get(Calendar.MONTH),
                                currentTime.get(Calendar.DAY_OF_MONTH), 19, 0, 0)
                            mCalendar.timeZone = TimeUtils.getCurrentTimeZone()
                            mCalendar.timeInMillis
                        }

                        else -> {
                            Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_specificTimeAndDate)
                            0
                        }
                    }

                modelChooseTime.setCalendar(milisec)
            }
        }

        view.takeDevice.setOnClickListener { view ->
            modelChooseTime.tryBookDevice().observe(this, androidx.lifecycle.Observer {
                if (it == true) {
                    view.showSnackbar(resources.getString(R.string.device_booked_message))
                    val action =
                        ChooseTimeFragmentDirections.actionChooseTimeFragmentToBackDeviceFragment(
                            modelChooseTime.userId.value!!,
                            modelChooseTime.chooseCalendar.value!!.timeInMillis)
                    startForegroundServiceNotification(modelChooseTime.chooseCalendar.value!!.timeInMillis)
                    context!!.startScheduleNotification(modelChooseTime.chooseCalendar.value!!)
                    Navigation.findNavController(view).navigate(action)
                } else {
                    view.showSnackbar(resources.getString(R.string.device_is_not_booked_message))
                }
            })
        }
        return view
        }

    private fun resetButton(button: Button) {
        button.setBackgroundResource(R.drawable.round_rectangle)
        button.setTextColor(resources.getColor(R.color.but_time_def))
    }

    private fun startForegroundServiceNotification(millisec: Long) {
        val serviceIntent = Intent(context?.applicationContext, NotificationService::class.java)
        serviceIntent.putExtra(INTENT_DATA_MILISEC, millisec)
        context?.startService(serviceIntent)
    }

    fun Context.startScheduleNotification(endTime: Calendar) {

        val deltaTime = GregorianCalendar.getInstance()
        deltaTime.timeInMillis = endTime.timeInMillis
        deltaTime.add(Calendar.MINUTE, -10)
        val currentTime = GregorianCalendar.getInstance()
        var delay: Long = if (deltaTime.timeInMillis < currentTime.timeInMillis) 1000L else deltaTime.timeInMillis - currentTime.timeInMillis
        val notificationId = 1
        val notification = createNotification(endTime, notificationId)
        val notificationIntentBroadcast = Intent(context?.applicationContext, MyNotificationPublisher::class.java)
        notificationIntentBroadcast.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntentBroadcast.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent =
            PendingIntent.getBroadcast(context?.applicationContext, notificationId, notificationIntentBroadcast, PendingIntent.FLAG_CANCEL_CURRENT)
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }

    private fun createNotification(endTime: Calendar, notificationId: Int): Notification {

        val format = SimpleDateFormat(DATE_FORMAT_NOTIFICATION_MESSAGE)
        val color = ContextCompat.getColor(context!!, R.color.background)
        val builder = NotificationCompat.Builder(context!!, CHANEL_ID)
            .setContentTitle(resources.getString(R.string.dont_forget_notification_title))
            .setContentText("${resources.getString(R.string.time_is_up_notification_title)} ${format.format(endTime.time)}")
            .setSmallIcon(R.drawable.alarm_clock)
            //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(color)
            .setAutoCancel(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.priority = NotificationCompat.PRIORITY_LOW
        }

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntentActivity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(pendingIntentActivity)
        return builder.build()
    }
}