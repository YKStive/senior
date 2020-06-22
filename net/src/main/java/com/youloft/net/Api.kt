package com.youloft.net

import com.youloft.net.bean.LoginBean
import retrofit2.Call
import retrofit2.http.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 接口定义
 */
interface Api {
    companion object {
        const val BASE_URL = ""
    }

    @POST("/api/user/login")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Call<LoginBean>
}