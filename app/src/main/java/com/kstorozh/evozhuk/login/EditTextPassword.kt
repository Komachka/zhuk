package com.kstorozh.evozhuk.login

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.kstorozh.evozhuk.R
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.text.InputType
import android.widget.EditText
import com.kstorozh.evozhuk.utils.onTextChanged

const val END_IMAGE_INDEX = 2
const val VISIBLE_PASSWORD_TYPE = InputType.TYPE_CLASS_TEXT
const val INVISIBLE_PASSWORD_TYPE = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
class EditTextPassword : AppCompatEditText {
    private lateinit var hintPassImg: Drawable
    private var iconStartCoordinate: Float = 0F

    constructor(context: Context) : super(context) { initContext() }
    constructor(context: Context, attr: AttributeSet) : super(context, attr) { initContext() }
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr) { initContext() }

    private fun initContext() {
        hintPassImg = ResourcesCompat.getDrawable(resources, R.mipmap.ic_hide, null)!!
        iconStartCoordinate = ((width - paddingRight - hintPassImg.intrinsicWidth).toFloat())
        onTextChanged { start -> if (start == 0) hideImage() else showImage() }
        handleShowPassOnIconClick()
    }

    private fun handleShowPassOnIconClick() {
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (!checkIfImageExists()) return false
                var isShowPassIconClicked = false
                if (event.x > iconStartCoordinate) { isShowPassIconClicked = true }
                if (isShowPassIconClicked) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        inputType = VISIBLE_PASSWORD_TYPE
                        showImage()
                    }
                    if (event.action == MotionEvent.ACTION_UP) {
                        inputType = INVISIBLE_PASSWORD_TYPE
                        showImage()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun checkIfImageExists(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            compoundDrawablesRelative[END_IMAGE_INDEX] != null
        } else {
            compoundDrawables[END_IMAGE_INDEX] != null
        }
    }

    private fun showImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                hintPassImg, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, hintPassImg, null)
        }
    }

    private fun hideImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                null, null
            )
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, hintPassImg, null)
        }
    }

}