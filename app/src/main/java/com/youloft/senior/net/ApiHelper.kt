package com.youloft.senior.net

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.facebook.stetho.Stetho
import com.youloft.coolktx.jsonToObject
import com.youloft.net.BaseRetrofitClient
import com.youloft.net.ParamsInterface
import com.youloft.senior.BuildConfig
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.utils.UserManager
import com.youloft.util.MD5
import okhttp3.*
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
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
        errorBlock: ((msg: String) -> Unit)? = null
    ) {
        if (response.isSuccess()) {
            successBlock.invoke(response.data)
        } else {
            errorBlock?.invoke(response.msg)
        }
    }


    /**
     * 处理所有51wnl-cq.com下请求的公共参数
     */
    val sParamsInterceptor: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
            if (request.url.host.toLowerCase().endsWith("51wnl-cq.com")) {
                //自己的接口 加入过期判断
                if (UserManager.instance.hasLogin() && !request.url.toString()
                        .contains("refreshtoken")
                ) {
                    if (UserManager.instance.hasExpiration()) {
                        val token = api.refreshToken(
                            UserManager.instance.getRefreshToken(),
                            UserManager.instance.getUserId()
                        )
                        if (token != null && token.getIntValue("status") == 200) {
                            if (token.getJSONObject("data") != null) {
                                val bean: LoginBean? =
                                    token.getJSONObject("data").toJSONString().jsonToObject()
                                if (bean != null) {
                                    UserManager.instance.refreshUserData(bean)
                                } else {
                                    UserManager.instance.loginOut(true)
                                    throw Exception("登录过期")
                                }
                            } else {
                                UserManager.instance.loginOut(true)
                                throw Exception("登录过期")
                            }
                        } else {
                            UserManager.instance.loginOut(true)
                            throw Exception("登录过期")
                        }
                    }
                }
            }
            //处理公共参数
            request = parseRequest(request)
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
        //添加header
        if (!TextUtils.isEmpty(UserManager.instance.getAccessToken())) {
            builder.addHeader(
                "Authorization",
                String.format("Bearer %s", UserManager.instance.getAccessToken())
            )
        }

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
        //验签实现
        builder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                if (request.url.host.toLowerCase().endsWith("51wnl-cq.com")) {
                    try {
                        val body = request.body
                        val queryString = "?" + request.url.encodedQuery
                        var bodyString: String? = ""
                        val isPost =
                            request.method.equals("post", ignoreCase = true)

                        //如果是 post 请求且 body 大于50k 则不进行签名参数生成
                        val isBigBody =
                            isPost && body != null && body.contentLength() > 51200
                        if (isPost) {
                            bodyString =
                                if (isBigBody) null else toBodyString(body)
                        }
                        //bodyString 为空则表示读取 body 部分出现故障则整体不生成签名
                        if (bodyString != null) {
                            //进行签名同时传递参数
                            val secret: String =
                                MD5.encode(queryString + bodyString + "youloft_2020").toUpperCase()
                            request = request.newBuilder().addHeader("hsign", secret).addHeader(
                                "av",
                                BuildConfig.VERSION_NAME
                            ).build()
                        }
                    } catch (e: Throwable) {
                        Log.e("SignError", "需要进行检查", e)
                    }
                }
                return chain.proceed(request)
            }
        })
        builder.addInterceptor(sParamsInterceptor)
    }

    /**
     * 读取 Body 内容
     *
     * @param body
     * @return
     */
    private fun toBodyString(body: RequestBody?): String? {
        if (body == null) {
            return ""
        }
        val bos = ByteArrayOutputStream()
        return try {
            val buffer = bos.sink().buffer()
            body.writeTo(buffer)
            buffer.flush()
            buffer.close()
            bos.toString("UTF-8")
        } catch (e: Exception) {
            null
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
            }
        }
    }
}