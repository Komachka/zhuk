package com.kstorozh.evozhuk.backDevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kstorozh.domainapi.model.SessionData
import java.text.SimpleDateFormat
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.notifications.NotificationService
import java.util.*
import kotlinx.android.synthetic.main.fragment_back_device.*
import kotlinx.android.synthetic.main.fragment_back_device.view.*
import kotlinx.android.synthetic.main.logo_and_info.*
import kotlinx.android.synthetic.main.logo_and_info.view.*

class BackDeviceFragment : Fragment() {

    lateinit var modelBackDevice: BackDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_back_device, container, false)
        view.infoImageBut.setOnClickListener {
                Navigation.findNavController(view).navigate(BackDeviceFragmentDirections.actionBackDeviceFragmentToInfoFragment())
        }
        view.deviceNameTv.text = context.getDeviceName()
        view.youTakeDeviceLabelTv.text = "${resources.getString(R.string.youTableDeviceLabel)} ${context.getDeviceName()}"

        modelBackDevice = ViewModelProviders.of(activity!!).get(BackDeviceViewModel::class.java)



        observe(modelBackDevice.getSessionData(), {
            it?.let {
                val format = SimpleDateFormat(DATE_FORMAT_BACK_DEVICE_SCREEN_TV)
                dateToBack.text = format.format(it.endData.time)
            }
        })


        /*modelBackDevice.getSessionData().observe(this, Observer {
            it?.let {
                val format = SimpleDateFormat(DATE_FORMAT_BACK_DEVICE_SCREEN_TV)
                dateToBack.text = format.format(it.endData.time)
            }
        })*/

        arguments?.let {
            val (endDate, userId) = Pair(
                BackDeviceFragmentArgs.fromBundle(arguments!!).endTime,
                BackDeviceFragmentArgs.fromBundle(arguments!!).userId)

            val endCalendar = GregorianCalendar.getInstance()
            endCalendar.timeInMillis = endDate
            modelBackDevice.setBookingSession(SessionData(userId, endCalendar))
        }

        view.giveBackBut.setOnClickListener { view ->

            modelBackDevice.tryReturnDevice().observe(this, Observer {
                if (it) {
                    view.showSnackbar(resources.getString(R.string.device_returned_message))
                    clearAllNotification(context)
                    stopForegroundService()
                    Navigation.findNavController(view).navigate(R.id.action_backDeviceFragment_to_returnDeviceFragment)
                } else
                    view.showSnackbar(resources.getString(R.string.device_not_returned_message))
            })
        }

        return view
    }

    private fun clearAllNotification(context: Context?) {
        val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun stopForegroundService() {
        val serviceIntent = Intent(context, NotificationService::class.java)
        serviceIntent.putExtra(INTENT_STOP_FLAG, INTENT_STOP_FLAG)
        context!!.stopService(serviceIntent)
    }
}