package com.youloft.senior.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Parcelable
import androidx.preference.PreferenceManager;
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youloft.senior.base.App
import kotlinx.android.parcel.Parcelize
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author you
 * @create 2020/6/18
 * @desc 属性代理，需要存入sp的属性，使用方法：val xx by Preference(key,default)，即调用了sp.get()
 * 属性赋值时，自动调用sp.set()
 */

class Preference<T>(val name: String, private val default: T) : ReadWriteProperty<Any?, T> {


    companion object {
        const val IS_LOGIN = "is_login"
        const val IS_AGREE_PRIVACY = "is_agree_privacy"
        const val USER_INFO = "user_gson"
        const val ACCESSTOKEN = "accessToken"
        const val REFRESHTOKEN = "refreshToken"
        const val EXPIRATION = "expiration"
        const val USER_PHONE = "user_phone"
        const val USER_ID = "user_id"
        const val USER_AVATAR = "user_avatar"
        const val USER_NICK_NAME = "user_nick_name"
        const val IS_PUNCH = "is_punch"
        const val LAST_PUBLISH_ALBUM_TIME = "last_publish_album_time"
        const val PUBLISHED_ALBUM_TIME = "published_album_time"

        private val prefs: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(App.instance())
        }

        fun setValue(key: String, property: KProperty<*>) {

        }

    }


    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun <T> putValue(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }


    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> deSerialization(getString(name, serialize(default))!!)
        }!!
        return res as T
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        prefs.edit().clear().apply()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        prefs.edit().remove(key).apply()
    }

    /**
     * 序列化对象
     * @param person
     * *
     * @return
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     * *
     * @return
     * *
     * @throws IOException
     * *
     * @throws ClassNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(): Map<String, *> {
        return prefs.all
    }
}