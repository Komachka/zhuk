package com.kstorozh.evozhuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crashlytics.android.Crashlytics
import com.kstorozh.evozhuk.chooseTime.ChooseTimeSharedViewModel
import com.kstorozh.evozhuk.login.LogInViewModel

import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.fragment_login.*

class MainActivity : AppCompatActivity() {

    val LOG_TAG = "MainActivity"
    lateinit var modelLogin: LogInViewModel
    lateinit var modelChooseTime: ChooseTimeSharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        val model = ViewModelProviders.of(this)[ErrorViewModel::class.java]

        model.getErrors().observe(
            this, Observer {
                Log.d(LOG_TAG, "Exception message ${it.throwable.message}")
                Toast.makeText(this, "Exception message ${it.throwable.message}", Toast.LENGTH_LONG).show()
            }
        )

        modelLogin = ViewModelProviders.of(this)[LogInViewModel::class.java]
        modelChooseTime = ViewModelProviders.of(this)[ChooseTimeSharedViewModel::class.java]


    }

    fun forceCrash(view: View) {
        throw RuntimeException("This is a crash")
    }
}
