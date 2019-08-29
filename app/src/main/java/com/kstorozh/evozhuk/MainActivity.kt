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

import kotlin.math.sqrt


class MainActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private var shake: Sensor? = null


    private val SHAKE_THRESHOLD_GRAVITY = 2.7f
    private val SHAKE_SLOP_TIME_MS = 500
    private val SHAKE_COUNT_RESET_TIME_MS = 3000


    private var mShakeTimestamp: Long = 0
    private var mShakeCount: Int = 0



    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // ignore
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        // gForce will be close to 1 when there is no movement.
        val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble())

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()
            // ignore shake events too close to each other (500ms)
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return
            }

            // reset the shake count after 3 seconds of no shakes
            if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                mShakeCount = 0
            }

            mShakeTimestamp = now
            mShakeCount++


            showDialog()
            //Toast.makeText(applicationContext, "mShakeCount $mShakeCount", Toast.LENGTH_LONG).show()
            Log.d(LOG_TAG, "mShakeCount $mShakeCount")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }


    override fun onResume() {
        super.onResume()
        shake?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    fun showDialog()
    {
        val mBottomSheetDialog = BottomSheetDialog(applicationContext)
        val sheetView = LayoutInflater.from(applicationContext).inflate(R.layout.bug_ot_wish_bottom_sheet_dialog, null)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }
}
