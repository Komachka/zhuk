package com.kstorozh.evozhuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import com.kstorozh.domainapi.HandleErrorUseCase
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.model.UserLoginInput

import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val usecaseLogin: LoginUseCase by inject()
    val handleErrors: HandleErrorUseCase by inject()

    val LOG_TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        val liveDataLogin = usecaseLogin.loginUser(UserLoginInput("Katya", "123"))
        liveDataLogin.observe(this, Observer {
            Log.d(LOG_TAG, it)
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        val liveDataError = handleErrors.getErrors()
        liveDataError.observe(this, Observer {
            Log.d(LOG_TAG, it.message)
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        })
    }

    fun forceCrash(view: View) {
        throw RuntimeException("This is a crash")
    }
}
