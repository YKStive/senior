package com.youloft.senior.utils

import com.youloft.util.DateUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * 超过一天
 * @receiver Calendar
 * @param time String
 * @return Boolean
 */
fun Calendar.moreThanOneDay(time: String): Boolean {
    val str2DateSafe = com.youloft.senior.utils.DateUtil.str2DateSafe(time)
    return this.timeInMillis - str2DateSafe.time > 24 * 60 * 3600
}