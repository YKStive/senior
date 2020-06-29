package com.youloft.senior.widgt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.youloft.senior.R
import com.youloft.util.UiUtil

/**
 * @author xll
 * @date 2020/6/22 11:00
 */
internal class CoinProcessView(
    context: Context?,
    attrs: AttributeSet?
) : View(context, attrs) {
    var max = 100
    var progress = 0
    var processIcon: Drawable
    var processDrawable: Drawable
    var paint: Paint
    var progressHeight: Int


    init {
        processIcon = resources.getDrawable(R.drawable.qd_jbcheck_icon)
        processDrawable = resources.getDrawable(R.drawable.top_coin_process_bg)
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        progressHeight = UiUtil.dp2Px(context, 6f)
    }

    fun setProcess(process: Int, count: Int) {
        var processValue = process;
        if (process > count) {
            processValue = count - 8000;
        }
        if (processValue < 0) {
            processValue = 0
        }
        this.progress = processValue
        this.max = count
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = processIcon.intrinsicHeight
        setMeasuredDimension(width, height)
    }

    var rect = RectF()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制底部
        paint.color = -0xd1e26
        val top = (height - progressHeight) / 2
        val bottom = top + progressHeight
        rect[0f, top.toFloat(), width.toFloat()] = bottom.toFloat()
        val r = UiUtil.dp2Px(context, 3f)
        canvas.drawRoundRect(rect, r.toFloat(), r.toFloat(), paint)
        //绘制上层
        val right = width * progress / max
        processDrawable.setBounds(0, top, right, bottom)
        processDrawable.draw(canvas)
        processIcon.setBounds(
            right - processIcon.intrinsicWidth / 2,
            0,
            right + processIcon.intrinsicWidth / 2,
            height
        )
        processIcon.draw(canvas)
    }

}