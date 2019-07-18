package com.kstorozh.evozhuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import com.kstorozh.domainapi.HandleErrorUseCase
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInputData

import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val usecaseLogin: LoginUseCase by inject()
    val handleErrors: HandleErrorUseCase by inject()

    val initDeviceUseCases: ManageDeviceUseCases by inject()

    val LOG_TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        initDeviceUseCases.initDevice(DeviceInputData("007", "sumsung", "android", "s8", 300, 300)).observe(
            this, Observer {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        )
    }

    fun forceCrash(view: View) {
        throw RuntimeException("This is a crash")
    }
}
