package com.kstorozh.evozhuk.chooseTime

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.backDevice.MyNotificationPublisher
import com.kstorozh.evozhuk.notifications.CHANEL_ID
import com.kstorozh.evozhuk.notifications.NotificationService
import com.kstorozh.evozhuk.utils.getDeviceName
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.fragment_time_choose.view.*
import kotlinx.android.synthetic.main.fragment_time_choose.view.buttonsRv
import kotlinx.android.synthetic.main.logo_and_info.view.*

class ChooseTimeFragment : Fragment(), HandleErrors {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.infoImageBut.setOnClickListener {
            Navigation.findNavController(view).navigate(ChooseTimeFragmentDirections.actionChooseTimeFragmentToInfoFragment())
        }
        view.deviceNameTv.text = context?.getDeviceName()
        view.youTakeDeviceLabelTv.text =
            "${resources.getString(R.string.time_choose_label)}${context?.getDeviceName()}?"
        val modelChooseTime = ViewModelProviders.of(activity!!).get(ChooseTimeViewModel::class.java)
        with(viewLifecycleOwner) {
            handleErrors(modelChooseTime, view)
        }
        view.calendarImageBut.setOnClickListener {
            arguments?.let {
                with(ChooseTimeFragmentDirections.actionChooseTimeFragmentToCalendarFragment(ChooseTimeFragmentArgs.fromBundle(it).userId)) {
                    Navigation.findNavController(view).navigate(this)
                }
            }
        }
        val anotherTimeMilisec = ChooseTimeFragmentArgs.fromBundle(arguments!!).milisec
        val userId = ChooseTimeFragmentArgs.fromBundle(arguments!!).userId
        Log.d(LOG_TAG, "userId $userId")
        if (userId != USER_ID_NOT_SET)
            modelChooseTime.setUserId(userId)
        modelChooseTime.setCalendar(anotherTimeMilisec)
        val res = context?.applicationContext?.resources as Resources
        val buttonTimes = listOf<TimeButton>(
            TimeButton(res.getString(R.string.oneHour), TimeUtils.getTimeInMsFromHours(1)).apply { if (anotherTimeMilisec == 0L) isSelected = true },
            TimeButton(res.getString(R.string.twoHour), TimeUtils.getTimeInMsFromHours(2)),
            TimeButton(res.getString(R.string.foutHour), TimeUtils.getTimeInMsFromHours(4)),
            TimeButton(res.getString(R.string.tillSevenOClock), TimeUtils.getTimeInMsForEndOfWorkDay()),
            TimeButton(res.getString(R.string.twoDays), TimeUtils.getTimeInMsFromHours(48)),
            TimeButton(res.getString(R.string.anotherTime), anotherTimeMilisec, navigation =
            { Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_specificTimeAndDate) }).apply {
                if (anotherTimeMilisec != 0L) isSelected = true }
        )
        viewManager = GridLayoutManager(this.context, SPAN_COUNT)
        viewAdapter = ButtonTimeAdapter(buttonTimes)
        view.buttonsRv.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(SpacesItemDecoration(SPACE_RECYCLER))
        }
        view.takeDevice.setOnClickListener { view ->
            val selected = buttonTimes.filter { timeBut -> timeBut.isSelected }
            viewLifecycleOwner.observe(modelChooseTime.tryBookDevice(selected.first().milisec)) {
                if (it) {
                    view.showSnackbar(resources.getString(R.string.device_booked_message))
                    val action =
                        ChooseTimeFragmentDirections.actionChooseTimeFragmentToBackDeviceFragment(
                            modelChooseTime.userIdLiveData.value!!,
                            modelChooseTime.chooseCalendar.value!!.timeInMillis)
                    startForegroundServiceNotification(selected.first().milisec)
                    context!!.startScheduleNotification(selected.first().milisec)
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
    }

    private fun startForegroundServiceNotification(millisec: Long) {
        val serviceIntent = Intent(context?.applicationContext, NotificationService::class.java)
        serviceIntent.putExtra(INTENT_DATA_MILISEC, millisec)
        context?.startService(serviceIntent)
    }

    fun Context.startScheduleNotification(endTime: Long) {
        val deltaTime = GregorianCalendar.getInstance()
        deltaTime.timeInMillis = endTime
        deltaTime.add(Calendar.MINUTE, -MUTUTS_BEFORE_TIME_IS_UP_NOTIFICATION)
        val currentTime = GregorianCalendar.getInstance()
        val delay: Long = if (deltaTime.timeInMillis < currentTime.timeInMillis) 1000L else deltaTime.timeInMillis - currentTime.timeInMillis
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

    private fun createNotification(endTime: Long, notificationId: Int): Notification {

        val format = SimpleDateFormat(DATE_FORMAT_NOTIFICATION_MESSAGE)
        val color = ContextCompat.getColor(context!!, R.color.background)
        val builder = NotificationCompat.Builder(context!!, CHANEL_ID)
            .setContentTitle(resources.getString(R.string.dont_forget_notification_title))
            .setContentText("${resources.getString(R.string.time_is_up_notification_title)} ${format.format(endTime)}")
            .setSmallIcon(R.drawable.alarm_clock)
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