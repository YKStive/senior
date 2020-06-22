package com.youloft.net

import android.content.Context
import com.facebook.stetho.Stetho
import okhttp3.OkHttpClient

/**
 * @author you
 * @create 2020/6/18
 * @desc 网络配置类
 * 参考 有其他api interface 需要另外配置
 * 调用方式:ApiHelper.api.xxx()
 */
object ApiHelper : BaseRetrofitClient() {

    val api by lazy {
        getService(Api::class.java, Api.BASE_URL) }
    fun initSteho(context: Context){
        Stetho.initializeWithDefaults(context);
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
    }
}