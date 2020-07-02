package com.youloft.senior.utils

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.jsonToObject
import com.youloft.coolktx.toJsonString
import com.youloft.senior.base.App
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.coin.CoinManager
import com.youloft.senior.push.PushWrapper
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.util.ToastMaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @author xll
 * @date 2020/6/30 13:27
 */

class UserManager {
    var bean: LoginBean? = null
    private var userInfo: String by Preference(Preference.USER_INFO, "")
    private var userInfoTime: Long by Preference("user_info_data_time", 0)

    init {
        bean = userInfo.jsonToObject()
    }


    /**
     * 登录成功
     */
    fun login(bean: LoginBean) {
        this.bean = bean
        userInfo = bean.toJsonString()
        userInfoTime = System.currentTimeMillis()
        CoinManager.instance.loadData()
        PushWrapper.updateAlias()
    }

    /**
     * 更新用户数据
     */
    fun refreshUserData(bean: LoginBean) {
        this.bean = bean
        userInfo = bean.toJsonString()
        userInfoTime = System.currentTimeMillis()
    }

    var showLogin: Boolean = false

    /**
     * 注销登录
     * @param reLogin 是否重新登录
     */
    fun loginOut(reLogin: Boolean = false) {
        this.bean = null
        userInfo = ""
        userInfoTime = -1
        PushWrapper.updateAlias()
        CoinManager.instance.loadData()
        if (reLogin) {
            //重新拉起登录界面
            synchronized(this) {
                if (showLogin) {
                    return
                }
                GlobalScope.launch(Dispatchers.Main) {
                    ToastMaster.showLongToast(App.instance(), "登录过期，请重新登录")
                    val activity = ActivityManager.instance.topActivity ?: return@launch
                    showLogin = true
                    LoginDialog(
                        activity as AppCompatActivity, activity.lifecycleScope
                    )
                        .apply { setOnDismissListener { showLogin = false } }
                        .show()
                }
            }
        }
    }

    /**
     * 是否登录
     */
    fun hasLogin(): Boolean {
        if (bean == null) {
            return false
        }
        return !TextUtils.isEmpty(bean!!.userId)
    }

    /**
     * 获取用户id
     */
    fun getUserId(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.userId
    }

    /**
     * 获取用户id
     */
    fun getAvatar(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.avatar
    }

    /**
     * 获取用户id
     */
    fun getNickName(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.nickname
    }

    /**
     * AccessToken是否过期
     */
    fun hasExpiration(): Boolean {
        if (bean == null) {
            return true
        }
        return abs(userInfoTime - System.currentTimeMillis()) > (bean!!.expiration * 1000 - 10 * 60 * 1000)
    }

    /**
     * 获取token
     */
    fun getAccessToken(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.accessToken
    }

    /**
     * 获取刷新token
     * ps：用于获取accessToken
     */
    fun getRefreshToken(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.refreshToken
    }

    companion object {
        /**
         * 获取实例
         *
         * @return
         */
        /**
         * 实例
         */
        val instance by lazy { UserManager() }

    }
}