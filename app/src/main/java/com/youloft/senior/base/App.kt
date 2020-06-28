package com.youloft.senior.base

import android.app.Application
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.youloft.senior.bean.User
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.push.PushWrapper
import com.youloft.senior.utils.Preference
import com.youloft.senior.web.WebComponentHandle
import com.youloft.webview.ProtocolDispatcher
import okhttp3.HttpUrl
import kotlin.properties.Delegates
import com.youloft.net.ParamsInterface as ParamsInterface1

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class App : Application() {
    var userId: String by Preference(Preference.USER_ID, "123")
    var user by Preference(Preference.USER_GSON, User.test())
    override fun onCreate() {
        super.onCreate()
        instance = this

//        初始化友盟分享
        UMConfigure.init(this, "kasjdlkjslakj", "", UMConfigure.DEVICE_TYPE_PHONE, "")
        UMConfigure.setLogEnabled(true)
        PlatformConfig.setWeixin(
            "wx5f3a0d4653cd3485",
            "af16a3c2d7b39dd4e8022e04ca1baa3f"
        )
        PushWrapper.init(this)


        ApiHelper.initSteho(this, object : ParamsInterface1 {
            override fun bindParams(urlBuilder: HttpUrl.Builder, params: Set<String>?) {
                bindParams1(urlBuilder, params)
            }
        })
        ProtocolDispatcher.registerProtocolHandle("protocol", WebComponentHandle::class.java)
    }

    private fun bindParams1(urlBuilder: HttpUrl.Builder, params: Set<String>?) {
        if (!hasKey(params, "uid")) {
            urlBuilder.addEncodedQueryParameter("uid", "qq20180530134654682")
        }

        if (!hasKey(params, "cid")) {
            urlBuilder.addEncodedQueryParameter("cid", "Youloft_Android")
        }
    }

    private fun hasKey(params: Set<String>?, key: String): Boolean {
        if (params == null) {
            return false;
        }
        return params.contains(key)
    }

    companion object {
        private var instance: App by Delegates.notNull()
        fun instance() = instance
    }

}