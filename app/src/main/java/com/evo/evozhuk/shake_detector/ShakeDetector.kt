package com.evo.evozhuk.shake_detector

import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import kotlin.math.sqrt

class ShakeDetector(private val mListener: OnShakeListener) : SensorEventListener {

    companion object {

        private const val SHAKE_THRESHOLD_GRAVITY = 2.7f
        private val SHAKE_SLOP_TIME_MS = 500
        private val SHAKE_COUNT_RESET_TIME_MS = 3000
    }

    private var mShakeTimestamp: Long = 0
    private var mShakeCount: Int = 0

    interface OnShakeListener {
        fun onShake(count: Int)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

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
            mListener.onShake(mShakeCount)
        }
    }
}