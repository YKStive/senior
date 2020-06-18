package com.youloft.senior.utils.gifmaker

import android.text.TextUtils
import java.lang.reflect.Field

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
internal object ReflectTool {
    fun getAnyByReflect(`object`: Any?, fieldName: String): Any? {
        if (`object` == null) {
            return null
        }
        if (TextUtils.isEmpty(fieldName)) {
            return null
        }
        var field: Field?
        var clazz: Class<*>? = `object`.javaClass
        while (clazz != Any::class.java) {
            try {
                field = clazz!!.getDeclaredField(fieldName)
                field.isAccessible = true
                return field.get(`object`)
            } catch (e: Exception) {
            }

            clazz = clazz!!.superclass
        }

        return null
    }
}