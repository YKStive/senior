package com.youloft.senior.push

import android.content.Intent
import com.umeng.message.UmengNotifyClickActivity
import com.umeng.message.entity.UMessage
import org.json.JSONObject

/**
 * 接收系统厂商点击操作
 * @author xll
 * @date 2020/6/17 12:53
 */
class NotifyActivity : UmengNotifyClickActivity() {
    override fun onMessage(intent: Intent) {
        super.onMessage(intent)
        try {
            val body = intent.getStringExtra("body")
            val bodyMessage = UMessage(JSONObject(body))
            UMPushMessageHandle.clickNotificationMessage(bodyMessage, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()
    }
}