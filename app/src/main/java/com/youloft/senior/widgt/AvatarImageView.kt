package com.youloft.senior.widgt

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.youloft.senior.R
import com.youloft.senior.base.App

class AvatarImageView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {
//    private var borderXfermode: PorterDuffXfermode
//    private var srcXfermode: PorterDuffXfermode
//    private var border: Bitmap? = null

    init {
//        scaleType = ScaleType.FIT_XY
//
//        srcXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
//        borderXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }


    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.RED
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val min = Math.min(
//            MeasureSpec.getSize(widthMeasureSpec),
//            MeasureSpec.getSize(heightMeasureSpec)
//        )
//        setMeasuredDimension(min, min)
//        if (border == null) {
//            val options = BitmapFactory.Options()
//            options.outWidth = min
//            options.outHeight = min
//            border = BitmapFactory.decodeResource(
//                App.instance().resources,
//                R.drawable.ic_avatar_border,
//                options
//            )
//        }
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
//        super.onDraw(canvas)
//        val circle = width / 2f
//        canvas.drawCircle(circle, circle, circle, paint)
//        paint.reset()
//        paint.xfermode = borderXfermode
//        canvas.drawBitmap(border!!,0f,0f,paint)
//        canvas.restore()
    }
}