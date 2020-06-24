package com.youloft.senior.net

import com.alibaba.fastjson.JSONObject
import com.google.gson.JsonObject
import com.youloft.net.bean.FavoriteHeadBean
import com.youloft.net.bean.NetResponse
import com.youloft.senior.bean.GifBean
import com.youloft.senior.bean.ItemDetailBean
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.bean.MissionResult
import retrofit2.Call
import retrofit2.http.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 接口定义
 */
interface Api {
    companion object {
        //        const val BASE_URL = "http://192.168.1.85:3000/mock/703/"
        const val BASE_URL = "https://shequ.51wnl-cq.com/"
    }

    @GET(BASE_URL + "api/Coin_Activity/GetMissions")
    fun getMissionInfo(): MissionResult?

    @GET(BASE_URL + "api/Coin_Activity/Complete")
    fun completeTask(
        @Query("code") code: String?,
        @Query("time") time: String?,
        @Query("sn") sn: String?,
        @Query("otherinfo") otherinfo: String? = null,
        @Query("extData") extData: String? = null,
        @Query("JlspOrderId") jlspOrderId: String? = null
    ): JsonObject?

    @GET(BASE_URL + "Api/User/GetCoinDetail")
    fun getCoinDetail(): JsonObject?

    @POST("/api/user/login")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Call<LoginBean>

    @GET("/api/post/comment")
    fun getCommentList(@QueryMap params: Map<String, String>): Call<ItemDetailBean>

    @GET("/api/post/praise")
    fun getFavoritetList(@QueryMap params: Map<String, String>): Call<FavoriteHeadBean>

    @GET("/api/post/{id}")
    fun getItem(@Path("id") id: String): Call<ItemDetailBean>

    /**
     * 获取提现金额列表
     */
    @GET(BASE_URL + "api/user/GetCashList")
    fun getCashList(): JSONObject?

    /**
     * 获取提现进度
     */
    @GET(BASE_URL + "api/user/CashProgress?Uid=phone20190924111851417523&caid=24&cid=Youloft_Android")
    fun getCashRecord(): JSONObject?

    /**
     * 获取用户金币信息
     */
    @GET(BASE_URL + "api/user/info")
    fun getUserCoinInfo(): JSONObject?

    @GET("/api/post/sticker")
    fun getStickers(): NetResponse<List<GifBean>>

    @POST(BASE_URL + "api/user/authcode")
    fun sendPhoneCode(@Body body: String): JSONObject?

    @POST(BASE_URL + "api/user/verifyPhoneCode")
    fun verifyPhoneCode(@Body body: String): JSONObject?

    /**
     * 提现接口
     */
    @GET(BASE_URL + "Api/User/WithDraw")
    fun withDraw(
        @Query("cash") cash: String?,
        @Query("type") type: Int?,
        @Query("cashtype") cashtype: Int?
    ): JSONObject?

}