package com.youloft.senior.widgt

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.jar.Attributes

/**
 * @author you
 * @create 2020/6/23
 * @desc  自动滑动的recycleView
 */
open class AutoScrollRecycleView(context: Context, attr: AttributeSet?) :
    RecyclerView(context, attr) {

    private var mHandler: PollHandler

    companion object {
        const val WHAT_AUTO_SCROLL = 0
        const val DELAY_TIME = 2000L
    }

    init {
        mHandler = PollHandler(this)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        val message = Message.obtain()
        message.what = WHAT_AUTO_SCROLL
        mHandler.sendMessageDelayed(message, DELAY_TIME)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }


    class PollHandler(private val mRecyclerView: RecyclerView) : Handler() {
        private var currentPosition = 0
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                WHAT_AUTO_SCROLL -> {
                    currentPosition++
                    if (currentPosition == mRecyclerView.adapter!!.itemCount) {
                        currentPosition = 0
                    }
                    mRecyclerView.smoothScrollToPosition(currentPosition)
                    val message = Message.obtain()
                    message.what = WHAT_AUTO_SCROLL
                    sendMessageDelayed(message, DELAY_TIME)
                }
            }
        }
    }
}