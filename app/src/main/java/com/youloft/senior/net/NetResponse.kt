package com.youloft.senior.net

/**
 * @author you
 * @create 2020/6/24
 * @desc 网络返回的基类
 */
data class NetResponse<out T>(val data: T, val sign: String, val msg: String, val status: Int) {
    fun isSuccess(): Boolean {
        return status == 200
    }

}