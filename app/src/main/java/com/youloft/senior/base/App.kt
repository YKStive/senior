package com.youloft.senior.base

import android.app.Application
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.youloft.net.ApiHelper
import com.youloft.senior.push.PushWrapper
import com.youloft.senior.utils.Preference
import com.youloft.senior.web.WebComponentHandle
import com.youloft.webview.ProtocolDispatcher
import kotlin.properties.Delegates

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class App : Application() {
    var userId: String by Preference(Preference.USER_ID, "123")
    override fun onCreate() {
        super.onCreate()
        instance = this

        //初始化友盟分享
        UMConfigure.init(this, "kasjdlkjslakj", "", UMConfigure.DEVICE_TYPE_PHONE, "")
        UMConfigure.setLogEnabled(true)
        PlatformConfig.setWeixin(
            "wx5f3a0d4653cd3485",
            "af16a3c2d7b39dd4e8022e04ca1baa3f"
        )
        PushWrapper.init(this)
        ApiHelper.initSteho(this)
        ProtocolDispatcher.registerProtocolHandle("protocol", WebComponentHandle::class.java)
    }

    companion object {
        private var instance: App by Delegates.notNull()
        fun instance() = instance
    }

}