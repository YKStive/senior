package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatImageView

/**
 * 动画不动的处理
 *
 * @VERSION 1.0.0
 * @AUTHOR: xll create 2018/3/14 13:49
 */
class AnimationImageView(
    context: Context?,
    attrs: AttributeSet?
) : AppCompatImageView(context, attrs) {
    var mAnimation: Animation? = null
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        super.clearAnimation()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mAnimation != null) {
            super.clearAnimation()
            super.startAnimation(mAnimation)
        }
    }

    override fun onVisibilityChanged(
        changedView: View,
        visibility: Int
    ) {
        super.onVisibilityChanged(changedView, visibility)
        super.clearAnimation()
        if (visibility == View.VISIBLE && mAnimation != null) {
            super.startAnimation(mAnimation)
        }
    }

    override fun startAnimation(animation: Animation) {
        mAnimation = animation
        super.startAnimation(animation)
    }

    override fun setAnimation(animation: Animation) {
        mAnimation = animation
        super.setAnimation(animation)
    }

    override fun clearAnimation() {
        mAnimation = null
        super.clearAnimation()
    }
}