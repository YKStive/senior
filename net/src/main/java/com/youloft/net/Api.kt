package com.youloft.net

import com.youloft.net.bean.LoginBean
import retrofit2.Call
import retrofit2.http.*

import com.google.gson.JsonObject
import com.youloft.net.bean.ItemDetailBean
import com.youloft.net.bean.MissionResult
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author you
 * @create 2020/6/18
 * @desc 接口定义
 */
interface Api {
    companion object {
        const val BASE_URL = "https://shequ.51wnl-cq.com/"
    }

    @GET(Api.BASE_URL + "api/Coin_Activity/GetMissions")
    fun getMissionInfo(): MissionResult?

    @GET(BASE_URL + "api/Coin_Activity/Complete")
    fun completeTask(
        @Query("code") code: String?,
        @Query("time") time: String?,
        @Query("sn") sn: String?,
        @Query("otherinfo") otherinfo: String? = null,
        @Query("extData") extData: String? = null
    ): JsonObject?

    @POST("/api/user/login")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Call<LoginBean>

    @GET("/api/post/comment")
    fun getCommentList(@QueryMap params: Map<String, String>): Call<ItemDetailBean>
}