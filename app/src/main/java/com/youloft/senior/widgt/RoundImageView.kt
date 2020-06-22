package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.TintContextWrapper

/**
 * @author you
 * @create 2020/6/22
 * @desc
 */
class RoundImageView(context: Context) : AppCompatImageView(context) {

    fun RoundImageView(context: Context?) {
        RoundImageView(context, null)
    }

    fun RoundImageView(
        context: Context?,
        attrs: AttributeSet?
    ) {
        RoundImageView(context, attrs, 0)
    }

    fun RoundImageView(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        AppCompatImageView(context, attrs, defStyleAttr)
    }
}