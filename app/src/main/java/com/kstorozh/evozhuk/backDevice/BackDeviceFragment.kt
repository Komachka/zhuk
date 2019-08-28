package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.domainapi.model.SessionData
import java.text.SimpleDateFormat
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.notifications.NotificationService
import com.kstorozh.evozhuk.utils.getDeviceName
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import java.util.*
import kotlinx.android.synthetic.main.fragment_back_device.*
import kotlinx.android.synthetic.main.fragment_back_device.view.*
import kotlinx.android.synthetic.main.logo_and_info.view.*
import org.joda.time.DateTime

class BackDeviceFragment : Fragment(), HandleErrors {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_back_device, container, false)
    }

    private var userId: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.infoImageBut.setOnClickListener {
            Navigation.findNavController(view).navigate(BackDeviceFragmentDirections.actionBackDeviceFragmentToInfoFragment())
        }
        view.calendarImageBut.setOnClickListener {
            if (userId != null) {
                with(BackDeviceFragmentDirections.actionBackDeviceFragmentToCalendarFragment(userId!!)) {
                    Navigation.findNavController(view).navigate(this)
                }
            }

        }
        view.deviceNameTv.text = context?.getDeviceName()
        view.youTakeDeviceLabelTv.text = "${resources.getString(R.string.youTableDeviceLabel)} ${context?.getDeviceName()}"
        val modelBackDevice = ViewModelProviders.of(this).get(BackDeviceViewModel::class.java)
        viewLifecycleOwner.handleErrors(modelBackDevice, view)
        viewLifecycleOwner.observe(modelBackDevice.getSessionData(), {
            it?.let {
                val format = SimpleDateFormat(DATE_FORMAT_BACK_DEVICE_SCREEN_TV)
                dateToBack.text = format.format(it.endData.time)
                userId = it.userId
            }
        })
        arguments?.let {
            val (endDate, userId) = Pair(
                BackDeviceFragmentArgs.fromBundle(it).endTime,
                BackDeviceFragmentArgs.fromBundle(it).userId)

            val endCalendar = GregorianCalendar.getInstance()
            endCalendar.timeInMillis = endDate
            if (userId != USER_ID_NOT_SET) // set only if came from choose time fragment
                modelBackDevice.setBookingSession(SessionData(userId, endCalendar))
        }
        view.giveBackBut.setOnClickListener { view ->
            viewLifecycleOwner.observe(modelBackDevice.tryReturnDevice()) {
                if (it) {
                    view.showSnackbar(resources.getString(R.string.device_returned_message))
                    clearAllNotification(context)
                    stopForegroundService()
                    Navigation.findNavController(view).navigate(R.id.action_backDeviceFragment_to_returnDeviceFragment)
                } else
                    view.showSnackbar(resources.getString(R.string.device_not_returned_message))
            }
        }
        reNewBut.setOnClickListener { view ->
            viewLifecycleOwner.observe(modelBackDevice.getNearbyBooking()) {
                it.getContentIfNotHandled()?.let {
                    if (it) {
                        viewLifecycleOwner.observe(modelBackDevice.getSessionData(), { session ->
                            session?.let {
                                Navigation.findNavController(view).navigate(
                                    BackDeviceFragmentDirections.actionBackDeviceFragmentToSpecificTimeAndDate(
                                        session.endData.timeInMillis,
                                        modelBackDevice.nearbyBooking.value!!.startDate,
                                        BACK_DEVICE_FRAGMENT_DIR
                                    )
                                )
                            }
                        })
                    } else {
                        Navigation.findNavController(view).navigate(
                            BackDeviceFragmentDirections.actionBackDeviceFragmentToSpecificTimeAndDate(
                                DateTime().plusMonths(2).millis,
                                DateTime().millis,
                                BACK_DEVICE_FRAGMENT_DIR
                            ))
                    }
                }
            }
        }
    }

    private fun clearAllNotification(context: Context?) {
        val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun stopForegroundService() {
        NotificationService.isServiceRunning = false
        val serviceIntent = Intent(context, NotificationService::class.java)
        serviceIntent.putExtra(INTENT_STOP_FLAG, INTENT_STOP_FLAG)
        context?.stopService(serviceIntent)
    }
}