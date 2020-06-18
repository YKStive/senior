package com.youloft.net

import okhttp3.OkHttpClient

/**
 * @author you
 * @create 2020/6/18
 * @desc 网络配置类
 * 参考 有其他api interface 需要另外配置
 * 调用方式:ApiHelper.api.xxx()
 */
class ApiHelper : BaseRetrofitClient() {

    val api by lazy { getService(Api::class.java, Api.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {

    }
}