package com.youloft.net

import com.youloft.net.raw.RawCallAdapterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.youloft.net.retofit.fastjson.FastJsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author you
 * @create 2020/6/18
 * @desc 网路配置基类
 */
abstract class BaseRetrofitClient {

    companion object {
        private const val TIME_OUT = 10
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor())
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            builder.addNetworkInterceptor(logging)
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            handleBuilder(builder)

            return builder.build()
        }

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(RawCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(FastJsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build().create(serviceClass)
    }
}
