package com.youloft.senior.base

import android.app.Application
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.youloft.senior.BuildConfig
import com.youloft.senior.bean.User
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.push.PushWrapper
import com.youloft.senior.utils.ActivityManager
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.UserManager
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
    var userId: String by Preference(Preference.USER_ID, "")
    var avatar: String by Preference(Preference.USER_AVATAR, "")
    var nickName: String by Preference(Preference.USER_NICK_NAME, "")
    var user by Preference(Preference.USER_GSON, User.test())
    override fun onCreate() {
        super.onCreate()
        instance = this

//        初始化友盟分享
        UMConfigure.init(
            this,
            "5ef993fbdbc2ec08212b6815",
            "",
            UMConfigure.DEVICE_TYPE_PHONE,
            "2438c7b5bd38b294f4ceb1f9c0f8c796"
        )
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        PushWrapper.init(this)
        PlatformConfig.setWeixin(
            "wx7b54fe6514eaefa9",
            "2db37220946d12f00d77f3185d3000eb"
        )
//        PushWrapper.init(this)


        ApiHelper.initSteho(this, object : ParamsInterface1 {
            override fun bindParams(urlBuilder: HttpUrl.Builder, params: Set<String>?) {
                bindParams1(urlBuilder, params)
            }
        })
        ProtocolDispatcher.registerProtocolHandle("protocol", WebComponentHandle::class.java)
        ActivityManager.instance.init(this)
    }

    private fun bindParams1(urlBuilder: HttpUrl.Builder, params: Set<String>?) {
        if (!hasKey(params, "uid")) {
            urlBuilder.addEncodedQueryParameter("uid", UserManager.instance.getUserId())
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