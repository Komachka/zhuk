package com.kstorozh.evozhuk

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import android.hardware.SensorManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kstorozh.evozhuk.shake_detector.ShakeDetector

import kotlin.math.sqrt


class MainActivity : AppCompatActivity(), ShakeDetector.OnShakeListener {
    override fun onShake(count: Int) {
        showDialog()
        //Toast.makeText(applicationContext, "Hello", Toast.LENGTH_LONG).show()
    }


    private lateinit var sensorManager: SensorManager
    private var shake: Sensor? = null
    private var shakeDetector: ShakeDetector? = null
    
    


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector = ShakeDetector(this)
    }


    override fun onResume() {
        super.onResume()
        shake?.also { light ->
            sensorManager.registerListener(shakeDetector, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }


    fun showDialog()
    {
        val mBottomSheetDialog = BottomSheetDialog(this@MainActivity)
        val sheetView = LayoutInflater.from(this@MainActivity).inflate(R.layout.bug_ot_wish_bottom_sheet_dialog, null)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }
}
