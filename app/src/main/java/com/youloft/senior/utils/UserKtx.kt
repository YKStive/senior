package com.youloft.senior.utils

import com.youloft.senior.base.App

/**
 * @author you
 * @create 2020/6/22
 * @desc 用户工具类
 */

fun String.isByUser(): Boolean {
    return App.instance().userId == this
}