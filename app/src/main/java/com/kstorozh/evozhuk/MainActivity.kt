package com.kstorozh.evozhuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crashlytics.android.Crashlytics
import com.kstorozh.evozhuk.chooseTime.ChooseTimeSharedViewModel

import io.fabric.sdk.android.Fabric

class MainActivity : AppCompatActivity() {

    lateinit var modelChooseTime: ChooseTimeSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        val errorModel = ViewModelProviders.of(this)[ErrorViewModel::class.java]
        errorModel.getErrors().observe(
            this, Observer {
                Log.d(LOG_TAG, "Exception message ${it.throwable?.message}")
                findViewById<View>(R.id.main).showErrorMessage(it)
            }
        )
        modelChooseTime = ViewModelProviders.of(this)[ChooseTimeSharedViewModel::class.java]
    }

    fun forceCrash(view: View) {
        throw RuntimeException("This is a crash")
    }
}
