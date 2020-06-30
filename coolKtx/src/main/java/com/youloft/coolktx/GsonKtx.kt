package com.youloft.coolktx

import com.google.gson.Gson

/**
 * @author xll
 * @date 2020/6/30 11:45
 */

private val gsonWrapper by lazy {
    Gson();
}

fun Any?.toJsonString(): String? {
    this ?: return null;
    return gsonWrapper.toJson(this)
}
