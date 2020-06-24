package com.youloft.senior.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintAttribute
import androidx.constraintlayout.widget.ConstraintLayout
import com.youloft.senior.R
import kotlinx.android.synthetic.main.item_common_title.view.*

/**
 * @author you
 * @create 2020/6/24
 * @desc
 */
class CommonTitle(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_common_title, this)

    }

    fun onBack(back: () -> Unit) {
        tv_title.setOnClickListener { back() }
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }
}