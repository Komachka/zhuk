package com.kstorozh.evozhuk.returnDevice

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.view_snackbar_low_battery.view.*

class LowBatterySnackbarView : ConstraintLayout, ContentViewCallback {

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_snackbar_low_battery, this)
        clipToPadding = false
    }

    override fun animateContentOut(dely: Int, duration: Int) {}

    override fun animateContentIn(dely: Int, duration: Int) {
        val scaleX = ObjectAnimator.ofFloat(this.info_image, View.SCALE_X, 0f, 1.5f)
        val scaleY = ObjectAnimator.ofFloat(this.info_image, View.SCALE_Y, 0f, 1.5f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }
}