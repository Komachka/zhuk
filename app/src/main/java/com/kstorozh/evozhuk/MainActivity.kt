package com.kstorozh.evozhuk

import android.content.Context
import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import android.hardware.SensorManager
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kstorozh.evozhuk.shake_detector.ShakeDetector
import com.kstorozh.evozhuk.utils.observe
import com.kstorozh.evozhuk.utils.showSnackbar
import kotlinx.android.synthetic.main.bug_ot_wish_bottom_sheet_dialog.view.*

class MainActivity : AppCompatActivity(), ShakeDetector.OnShakeListener {
    override fun onShake(count: Int) {
        showDialog()
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var mBottomSheetDialog: BottomSheetDialog
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
        createDialog()
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

    private fun createDialog() {
        val model = ViewModelProviders.of(this)[BaseViewModel::class.java]
        var state = WISH
        mBottomSheetDialog = BottomSheetDialog(this@MainActivity)
        val sheetView = LayoutInflater.from(this@MainActivity).inflate(R.layout.bug_ot_wish_bottom_sheet_dialog, null)

        sheetView.wish.isChecked = true
        sheetView.wishOrBug.setOnCheckedChangeListener { radiogroup, id ->
            state = if (id == R.id.wish) {
                WISH
            } else {
                BUG
            }
        }
        sheetView.sentBut.setOnClickListener { view ->
            observe(model.sendReport(state, sheetView.editText.text.toString())) { event ->
                event.getContentIfNotHandled()?.let {
                    if (it) {
                        view.showSnackbar(resources.getString(R.string.reportMessSuc))
                    }
                }
            }
            mBottomSheetDialog.cancel()
        }
        mBottomSheetDialog.setContentView(sheetView)
    }

    private fun showDialog() {
        if (!mBottomSheetDialog.isShowing)
            mBottomSheetDialog.show()
    }
}
