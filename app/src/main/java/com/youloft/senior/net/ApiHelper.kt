package com.youloft.senior.net

import android.content.Context
import com.facebook.stetho.Stetho
import com.youloft.net.BaseRetrofitClient
import com.youloft.net.ParamsInterface
import com.youloft.net.bean.NetResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 网络配置类
 * 参考 有其他api interface 需要另外配置
 * 调用方式:ApiHelper.api.xxx()
 */
object ApiHelper : BaseRetrofitClient() {
    var paramHandlers: ParamsInterface? = null
    val api by lazy {
        getService(Api::class.java, Api.BASE_URL)
    }

    fun initSteho(context: Context, paramHandlers: ParamsInterface) {
        Stetho.initializeWithDefaults(context);
        ApiHelper.paramHandlers = paramHandlers
    }

    /**
     * response统一返回
     * @param response NetResponse<T>
     */
    fun <T : Any> executeResponse(
        response: NetResponse<T>,
        successBlock: (data: T) -> Unit,
        errorBlock: (msg: String) -> Unit
    ) {
        if (response.isSuccess()) {
            successBlock.invoke(response.data)
        } else {
            errorBlock.invoke(response.msg)
        }
    }


    /**
     * 处理所有51wnl-cq.com下请求的公共参数
     */
    var sParamsInterceptor: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
//            if (request.url.host.toLowerCase().endsWith("51wnl-cq.com")) {
            //处理公共参数
            request = parseRequest(request)
//            }
            return chain.proceed(request)
        }
    }

    /**
     * 处理公共参数的添加
     *
     * @param request
     * @return
     */
    @Throws(IOException::class)
    private fun parseRequest(request: Request): Request {
        //公共参数只处理URL后面追加参数
        val builder = request.newBuilder()
        val urlBuilder = request.url.newBuilder()
        if (paramHandlers != null) {
            paramHandlers!!.bindParams(
                urlBuilder,
                wrapIgnoreCaseSet(request.url.queryParameterNames)
            )
        }
        builder.url(urlBuilder.build())
        return builder.build()
    }

    /**
     * 转换小写
     *
     * @param source
     * @return
     */
    private fun wrapIgnoreCaseSet(source: Set<String>?): Set<String>? {
        if (source == null || source.isEmpty()) return HashSet()
        val convertedSet = HashSet<String>()
        val iterator = source.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next() ?: continue
            convertedSet.add(next.toLowerCase())
        }
        return convertedSet
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.addInterceptor(sParamsInterceptor)
    }
}