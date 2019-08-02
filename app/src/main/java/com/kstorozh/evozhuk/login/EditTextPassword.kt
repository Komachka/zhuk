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


class EditTextPassword : AppCompatEditText {
    lateinit var hintPassImg: Drawable


    constructor(context: Context) : super(context) { initContext() }
    constructor(context: Context, attr: AttributeSet) : super(context, attr) { initContext() }
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr) { initContext() }

    private fun initContext() {
        hintPassImg = ResourcesCompat.getDrawable(resources, R.mipmap.ic_hide, null)!!
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start == 0)
                    hideImage()
                else
                    showImage()
            }
        })
        setOnTouchListener(object: OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (compoundDrawablesRelative[2] != null) {
                        val buttonStart: Float = ((width - paddingEnd - hintPassImg.intrinsicWidth).toFloat())
                        var isClearButtonClicked = false
                        if (event.x > buttonStart) { isClearButtonClicked = true}
                        if (isClearButtonClicked) {
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                    inputType = InputType.TYPE_CLASS_TEXT
                                    showImage()
                            }
                            if (event.action == MotionEvent.ACTION_UP) {
                                    inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                                    showImage()
                                    return true
                            }
                        } else
                            return false
                    }
                }
                else {
                   if(compoundDrawables[2] != null)
                   {

                   }
                }
                return false
            }


        })
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