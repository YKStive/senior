package com.youloft.senior.utils

import android.text.TextUtils
import com.youloft.coolktx.jsonToObject
import com.youloft.coolktx.toJsonString
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.coin.CoinManager
import kotlin.math.abs

/**
 * @author xll
 * @date 2020/6/30 13:27
 */

class UserManager {
    private var bean: LoginBean? = null
    private var userInfo: String by Preference("user_info_data", "")
    private var userInfoTime: Long by Preference("user_info_data_time", 0)

    init {
        bean = userInfo.jsonToObject()
    }

    fun login(bean: LoginBean) {
        this.bean = bean
        userInfo = bean.toJsonString()
        userInfoTime = System.currentTimeMillis()
        CoinManager.instance.loadData()
    }

    fun refreshUserData(bean: LoginBean) {
        this.bean = bean
        userInfo = bean.toJsonString()
        userInfoTime = System.currentTimeMillis()
    }

    fun loginOut() {
        this.bean = null
        userInfo = ""
        userInfoTime = -1
    }

    fun hasLogin(): Boolean {
        if (bean == null) {
            return false
        }
        return !TextUtils.isEmpty(bean!!.userId)
    }

    fun getUserId(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.userId
    }

    /**
     * 是否过期
     */
    fun hasExpiration(): Boolean {
        if (bean == null) {
            return true
        }
        return abs(userInfoTime - System.currentTimeMillis()) > (bean!!.expiration * 1000 - 10 * 60 * 1000)
    }

    fun getAccessToken(): String {
        if (bean == null) {
            return ""
        }
        return bean!!.accessToken
    }

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