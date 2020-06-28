package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.youloft.senior.utils.logD

/**
 * @author you
 * @create 2020/6/28
 * @desc 长宽一致的imageView
 */
class SquareImageView(context: Context, attributeSet: AttributeSet?) :
    AppCompatImageView(context, attributeSet) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        "width".logD()
    }
}