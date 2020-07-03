package com.youloft.senior.widgt

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author xll
 * @date 2020/7/3 9:33
 */
class PressTextView(
    context: Context?,
    attrs: AttributeSet?
) : AppCompatTextView(context, attrs) {
    private val helper = PressHelper(this)

    override fun dispatchSetPressed(pressed: Boolean) {
        super.dispatchSetPressed(pressed)
        helper.dispatchSetPressed(pressed)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        helper.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        helper.onDraw(canvas)
        super.onDraw(canvas)
    }
}