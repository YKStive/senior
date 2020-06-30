package com.youloft.coolktx

import com.google.gson.Gson

/**
 * @author xll
 * @date 2020/6/30 11:45
 */

val gsonWrapper by lazy {
    Gson();
}

fun Any?.toJsonStringEmpty(): String? {
    this ?: return null;
    return kotlin.runCatching { gsonWrapper.toJson(this) }.getOrNull()
}

fun Any?.toJsonString(): String {
    this ?: return ""
    return kotlin.runCatching { gsonWrapper.toJson(this) }.getOrDefault("")
}

inline fun <reified T> String?.jsonToObject(defaultValue: T? = null): T? {
    return kotlin.runCatching {
        gsonWrapper.fromJson(this, T::class.java)
    }.getOrDefault(defaultValue)
}