package com.evo.evozhuk.login

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.evo.evozhuk.R
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.text.InputType
import com.evo.evozhuk.utils.onTextChanged

const val END_IMAGE_INDEX = 2
const val VISIBLE_PASSWORD_TYPE = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL)
const val INVISIBLE_PASSWORD_TYPE = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
class EditTextPassword : AppCompatEditText {
    private lateinit var hintPassImg: Drawable
    private var iconStartCoordinate: Int = 0

    constructor(context: Context) : super(context) { initContext() }
    constructor(context: Context, attr: AttributeSet) : super(context, attr) { initContext() }
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr) { initContext() }

    private fun initContext() {
        hintPassImg = ResourcesCompat.getDrawable(resources, R.mipmap.ic_hide, null)!!
        onTextChanged { start -> if (start == 0) hideImage() else showImage() }
        handleShowPassOnIconClick()
    }

    private fun handleShowPassOnIconClick() {
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (!checkIfImageExists()) return false
                var isShowPassIconClicked = false
                iconStartCoordinate = width - paddingRight - hintPassImg.intrinsicWidth
                if (event.x > iconStartCoordinate) { isShowPassIconClicked = true }
                if (isShowPassIconClicked) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        inputType = VISIBLE_PASSWORD_TYPE
                        showImage()
                        setSelection(text!!.length)
                    }
                    if (event.action == MotionEvent.ACTION_UP) {
                        inputType = INVISIBLE_PASSWORD_TYPE
                        showImage()
                        setSelection(text!!.length)
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