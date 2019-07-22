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
import android.widget.Toast


class EditTextPassword: AppCompatEditText
{
    lateinit var hintPassImg: Drawable

    constructor(context:Context) : super(context) {initContext()}
    constructor(context: Context, attr:AttributeSet) : super(context, attr) {initContext()}
    constructor(context: Context, attr: AttributeSet, defStyleAttr:Int) : super(context, attr, defStyleAttr) {initContext()}

    private fun initContext() {
        hintPassImg =  ResourcesCompat.getDrawable(resources, R.mipmap.ic_hide, null)!!
        addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start == 0)
                    hideImage()
                else
                    showImage()
            }

        })
    }

    private fun showImage()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, // Above text.
                hintPassImg, null
            )
        }
    }

    private fun hideImage()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, // Above text.
                null, null
            )
        }
    }
}