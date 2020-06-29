package com.youloft.senior.net

import com.alibaba.fastjson.JSONObject
import com.youloft.net.bean.CommentBean
import com.youloft.senior.bean.GifBean
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.bean.MissionResult
import com.youloft.senior.bean.*
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

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
    ): JSONObject?

    @GET(BASE_URL + "Api/User/GetCoinDetail")
    fun getCoinDetail(): JSONObject?

    @POST("/api/user/login")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Call<LoginBean>

    /**
     * 获取帖子评论列表
     */
    @GET("api/post/comment")
    fun getCommentList(@QueryMap params: Map<String, String>): NetResponse<List<CommentBean>>

    @GET("api/post/praise")
    fun getFavoritetList(@QueryMap params: Map<String, String>): NetResponse<List<FavoriteHeadBean>>

    @GET("api/post/{id}")
    fun getItem(@Path("id") id: String): NetResponse<ItemData>

    /**
     * 获取提现金额列表
     */
    @GET(BASE_URL + "api/user/GetCashList")
    fun getCashList(): JSONObject?

    /**
     * 获取提现进度
     */
    @GET(BASE_URL + "api/user/CashProgress")
    fun getCashRecord(@Query("caid") caid: String): JSONObject?

    /**
     * 获取提现进度
     */
    @GET(BASE_URL + "Api/User/GetIsExistsCashingOrder")
    fun getLastCash(): JSONObject?

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
        @Query("cash") cash: Int?,
        @Query("type") type: Int?,
        @Query("cashtype") cashtype: Int?
    ): JSONObject?

    //    @GET("/api/post/user/list")
//    fun getMineList(  方向up:0|down:1
//        @Query("index") index: Int,
//        @Query("direction") direction: Int,
//        @Query("limit") limit: Int
//    ): NetResponse<List<MineDataBean>>
    @GET("api/post/user/{userId}/list")
    fun getMineList(@Path("userId") id: String
    ): NetResponse<List<MineDataBean>>


    /**
     * 提现进度
     *
     * @return
     */
    @GET(BASE_URL + "Api/User/CashProgress")
    fun cashProgress(@Query("caId") caId: String?): Observable<ApplyOptionResponse?>

    /**
     * 发布帖子
     * @param caId String?
     * @return NetResponse<Any>
     */
    @POST("/api/post")
    fun publishPost(post: Post): NetResponse<Any>
}