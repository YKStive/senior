package com.youloft.senior.net

import com.alibaba.fastjson.JSONObject
import com.youloft.net.bean.CommentBean
import com.youloft.senior.bean.*
import retrofit2.http.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 接口定义
 */
interface Api {
    companion object {
        //        const val BASE_URL = "http://192.168.1.85:3000/mock/703/"
//        const val BASE_URL = "http://120.27.20.114:8090/"
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
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun login(@Body params: LoginUploadData): NetResponse<LoginBean>

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
    @GET(BASE_URL + "api/user/refreshtoken")
    fun refreshToken(
        @Query("refreshToken") refreshToken: String
        , @Query("userId") userId: String
    ): JSONObject?

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
    fun sendPhoneCode(@Body body: JSONObject): JSONObject?

    @POST(BASE_URL + "api/user/verifyPhoneCode")
    fun verifyPhoneCode(@Body body: JSONObject): JSONObject?

    /**
     * 提现接口
     */
    @GET(BASE_URL + "Api/User/WithDraw")
    fun withDraw(
        @Query("cash") cash: Int?,
        @Query("type") type: Int?,
        @Query("cashtype") cashtype: Int?
    ): JSONObject?


    /**
     * 获取个人发布的帖子
     * @param params HashMap<String, String>
     * @return NetResponse<List<MineDataBean>>
     */
    @GET("api/post/user/list")
    fun getMineList(
        @QueryMap params: HashMap<String, String>
    ): NetResponse<List<MineDataBean>>

    /**
     * 发布帖子
     * @param caId String?
     * @return NetResponse<Any>
     */
    @POST("/api/post")
    fun publishPost(@Body post: Post): NetResponse<Any>

    /**
     * 点赞评论
     */
    @POST("api/post/comment/praise")
    fun parse(@Body params: PraiseComment): NetResponse<String>

    /**
     * 点赞帖子
     */
    @POST("api/post/praise")
    fun parsePost(@Body params: PraiseBean): NetResponse<String>

    /**
     * 评论贴子
     */
    @POST("api/post/comment")
    fun commnet(@Body params: PraiseBean): NetResponse<String>

    /**
     * 删除贴子
     */
    @POST("api/post/delete")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun deletePost(@Body postId: String): NetResponse<Boolean>

    /**
     * 删除评论
     */
    @POST("api/post/comment/delete")
    @FormUrlEncoded
    fun deletePost(
        @Field("postId") postId: String,
        @Field("commentId") commentId: String
    ): NetResponse<String>


    /**
     * 获取未读评论
     * @return NetResponse<List<Comment>>
     */
    @GET("/api/post/user/unreadcomment")
    fun getUnReadComment(): NetResponse<List<Comment>>

    /**
     * 获取7牛上传token
     */
    @GET("/api/user/getqnuploadtoken")
    fun getQNToken(): NetResponse<JSONObject>

    /**
     * 获取影集模板列表
     */
    @GET("api/Post/Albums")
    fun getMovieTemplateList(
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): NetResponse<List<MovieTemplateBean>>


}