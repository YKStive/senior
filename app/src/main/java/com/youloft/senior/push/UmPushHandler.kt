package com.youloft.senior.push

import android.content.Context
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage

/**
 * 友盟接收到消息的回调
 * @author xll
 * @date 2020/4/17 11:44
 */
class UmPushHandler : UmengMessageHandler() {
    override fun dealWithCustomMessage(
        context: Context,
        uMessage: UMessage
    ) {
        super.dealWithCustomMessage(context, uMessage)
        UMPushMessageHandle.handleMessageReceived(uMessage, context)
    }

    override fun dealWithNotificationMessage(
        context: Context,
        uMessage: UMessage
    ) {
        super.dealWithNotificationMessage(context, uMessage)
        UMPushMessageHandle.handleNotificationMessage(uMessage, context)
    }
}