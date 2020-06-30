package com.youloft.senior.utils

import android.text.TextUtils
import com.youloft.coolktx.jsonToObject
import com.youloft.senior.bean.LoginBean

/**
 * @author xll
 * @date 2020/6/30 13:27
 */

class UserManager {
    var bean: LoginBean? = null

    init {
        val userInfo: String by Preference(Preference.USER_GSON, "")
        bean = userInfo.jsonToObject()
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