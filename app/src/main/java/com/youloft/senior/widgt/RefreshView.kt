package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.youloft.senior.R
import kotlinx.android.synthetic.main.refresh_layout.view.*

/**
 * @author xll
 * @date 2020/6/30 10:34
 */
internal class RefreshView(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {
    private var callback: (() -> Unit)? = null
    private val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.mettle_rotate)
    private fun setState(state: Int) {
        anim_view.clearAnimation()
        if (state == STATE_REFREHSING) {
            setRefresh(true)
            isClickable = true
            visibility = View.VISIBLE
            anim_view.startAnimation(anim)
        } else if (state == STATE_ERROR) {
            setRefresh(false)
            visibility = View.VISIBLE
        } else if (state == STATE_SUCCESS) {
            empty_view.visibility = View.INVISIBLE
            visibility = View.INVISIBLE
        }
    }

    fun bindRefreshCallBack(callbcak: () -> Unit) {
        this.callback = callbcak
    }

    fun showErr() {
        setState(STATE_ERROR)
    }

    fun showSuccess() {
        setState(STATE_SUCCESS)
    }

    fun showLoading() {
        setState(STATE_REFREHSING)
    }

    private fun setRefresh(b: Boolean) {
        empty_view.visibility = if (b) View.INVISIBLE else View.VISIBLE
        refresh.visibility = if (b) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        /**
         * 刷新中
         */
        var STATE_REFREHSING = 0

        /**
         * 刷新失败
         */
        var STATE_ERROR = 1

        /**
         * 刷新成功
         */
        var STATE_SUCCESS = 2
    }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.refresh_layout, this, true)

        empty_view.setOnClickListener {
            setState(STATE_REFREHSING)
            if (this.callback != null) {
                this.callback!!.invoke()
            }
        }
        setState(STATE_SUCCESS)
    }
}