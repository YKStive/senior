package com.youloft.senior.widgt

import android.graphics.*
import android.graphics.drawable.StateListDrawable
import android.view.View

/**
 * @author xll
 * @date 2020/7/3 10:59
 */

class PressHelper(private val view: View) {
    private val savePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG.or(Paint.DITHER_FLAG).or(Paint.FILTER_BITMAP_FLAG)).apply {
            color = Color.parseColor("#0d000000")
            xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        }
    }

    private val saveRect by lazy {
        RectF(0f, 0f, 0f, 0f)
    }

    fun dispatchSetPressed(pressed: Boolean) {
        if (view.background is StateListDrawable) {
            return
        }
        view.invalidate()
    }

    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        saveRect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    fun onDraw(canvas: Canvas?) {
        if (view.isPressed && view.background != null && view.background !is StateListDrawable) {
            canvas?.apply {
                saveLayerAlpha(saveRect, 255)
                view.background.draw(this)
                drawRect(saveRect, savePaint)
                restore()
            }

        }
    }
}