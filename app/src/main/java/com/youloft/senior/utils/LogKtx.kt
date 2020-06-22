package com.youloft.senior.utils

/**
 * @author you
 * @create 2020/6/22
 * @desc 日志工具类
 */
import android.util.Log

const val TAG = "LogKtx"

fun String.logV(tag: String = TAG) {
    Log.v(tag, this)
}

fun String.logD(tag: String = TAG) {
    Log.d(tag, this)
}

fun String.logI(tag: String = TAG) {
    Log.i(tag, this)
}

fun String.logW(tag: String = TAG) {
    Log.w(tag, this)
}

fun String.logE(tag: String = TAG) {
    Log.e(tag, this)
}

fun logDebug(msg: String, tag: String = TAG) {
    Log.d(tag, msg)
}
