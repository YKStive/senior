package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.youloft.senior.R
import com.youloft.senior.base.App
import kotlinx.android.synthetic.main.item_post_remote_content_multi_image.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc  贴子--多图content
 */
class PostItemMultiImage : ConstraintLayout {


    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr, 0
    )

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    private fun initView() {
        val root = LayoutInflater.from(App.instance())
            .inflate(R.layout.item_post_remote_content_multi_image, this)

    }


}