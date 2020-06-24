package com.youloft.senior.bean

/**
 * 只有doubleCode代表执行双倍后的保存，有doubleCode 有posid 代表触发激励视频
 * @author xll
 * @date 2020/6/23 11:01
 */
class DoubleBean {
    var doubleCode: String? = null
    var appid: String? = null
    var posid: String? = null
    var cash: Boolean = false
}