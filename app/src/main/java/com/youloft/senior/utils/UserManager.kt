package com.youloft.senior.utils

import android.text.TextUtils
import com.youloft.coolktx.jsonToObject
import com.youloft.coolktx.toJsonString
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.coin.CoinManager

/**
 * @author xll
 * @date 2020/6/30 13:27
 */

class UserManager {
    var bean: LoginBean? = null
    var userInfo: String by Preference("user_info_data", "")

    init {
        bean = userInfo.jsonToObject()
    }

    fun login(bean: LoginBean) {
        this.bean = bean
        userInfo = bean.toJsonString()
        CoinManager.instance.loadData()
    }

    fun loginOut() {
        this.bean = null
        userInfo = ""
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