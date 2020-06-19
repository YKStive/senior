package com.youloft.senior.push

import android.content.Context
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage

/**
 * 友盟回调通知点击事件
 * @author xll
 * @date 2020/4/17 11:37
 */
class UMPushClickHandler : UmengNotificationClickHandler() {
    override fun handleMessage(
        context: Context,
        uMessage: UMessage
    ) {
        super.handleMessage(context, uMessage)
    }

    override fun dealWithCustomAction(
        context: Context,
        uMessage: UMessage
    ) {
        super.dealWithCustomAction(context, uMessage)
        UMPushMessageHandle.clickNotificationMessage(uMessage, context)
    }
}