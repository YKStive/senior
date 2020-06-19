package com.youloft.senior.push

import android.content.Context
import com.umeng.message.entity.UMessage

/**
 * @author xll
 * @date 2020/4/17 11:45
 */
object UMPushMessageHandle {
    private const val TAG = "JPushReceiver"

    /**
     * 处理接收到自定义消息
     */
    fun handleMessageReceived(
        uMessage: UMessage?,
        context: Context?
    ) {
    }

    /**
     * 接收到自定义消息
     *
     * @param uMessage
     * @param context
     */
    fun handleNotificationMessage(
        uMessage: UMessage?,
        context: Context?
    ) {
    }

    /**
     * 接收到点击事件，处理打开对应落地页
     *
     * @param uMessage
     * @param context
     */
    fun clickNotificationMessage(
        uMessage: UMessage?,
        context: Context?
    ) {
    }
}