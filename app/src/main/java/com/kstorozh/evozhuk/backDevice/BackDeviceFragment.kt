package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.logo_and_info.*
import kotlinx.android.synthetic.main.logo_and_info.view.*
import kotlinx.android.synthetic.main.logo_and_info.view.infoImageBut
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception

class BackDeviceFragment : Fragment(), HandleErrors {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_back_device, container, false)
    }

    private var userId: String? = null
    private val modelBackDevice: BackDeviceViewModel by viewModel()
    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        super.onViewCreated(fragment, savedInstanceState)
        infoImageBut.setOnClickListener {
            Navigation.findNavController(fragment)
                .navigate(BackDeviceFragmentDirections.actionBackDeviceFragmentToInfoFragment())
        }
        calendarImageBut.setOnClickListener {
            if (userId != null) {
                with(BackDeviceFragmentDirections.actionBackDeviceFragmentToCalendarFragment(userId!!)) {
                    Navigation.findNavController(fragment).navigate(this)
                }
            }
        }
        deviceNameTv.text = context?.getDeviceName()
        youTakeDeviceLabelTv.text =
            "${resources.getString(R.string.youTableDeviceLabel)} ${context?.getDeviceName()}"
        viewLifecycleOwner.handleErrors(modelBackDevice, fragment)
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
                BackDeviceFragmentArgs.fromBundle(it).userId
            )

            val endCalendar = GregorianCalendar.getInstance()
            endCalendar.timeInMillis = endDate
            if (userId != USER_ID_NOT_SET) // set only if came from choose time fragment
                modelBackDevice.setBookingSession(SessionData(userId, endCalendar))
        }
        giveBackBut.setOnClickListener { view ->
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
            viewLifecycleOwner.observe(modelBackDevice.getSessionData(), { session ->
                session?.let {
                    viewLifecycleOwner.observe(modelBackDevice.getNearbyBooking()) {
                        it.getContentIfNotHandled()?.let { isNearbyBookingExists ->
                            var endDate = DateTime().plusMonths(2).millis
                            if (isNearbyBookingExists) {
                                endDate = modelBackDevice.nearbyBooking.value!!.startDate
                            }
                            try {
                                Navigation.findNavController(fragment).navigate(
                                    BackDeviceFragmentDirections.actionBackDeviceFragmentToSpecificTimeAndDate(
                                        session.endData.timeInMillis,
                                        endDate,
                                        BACK_DEVICE_FRAGMENT_DIR
                                    )
                                )
                            } catch (e: Exception) {
                                Log.d(LOG_TAG, "Can't open 2 links at once!")
                            }
                        }
                    }
                }
            })
        }
    }

    private fun clearAllNotification(context: Context?) {
        val notificationManager: NotificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun stopForegroundService() {
        NotificationService.isServiceRunning = false
        val serviceIntent = Intent(context, NotificationService::class.java)
        serviceIntent.putExtra(INTENT_STOP_FLAG, INTENT_STOP_FLAG)
        context?.stopService(serviceIntent)
    }
}