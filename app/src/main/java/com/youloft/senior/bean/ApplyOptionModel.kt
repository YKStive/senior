package com.youloft.senior.bean

import com.google.gson.annotations.SerializedName


class ApplyOptionResponse() : IJsonObject {
    @SerializedName("data")
    var data: ApplyOption? = null
}

data class ApplyOption(
    @SerializedName("isSpeed")
    var isSpeed: Boolean, // true
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("speedConfig")
    var speedConfig: SpeedConfig?,
    @SerializedName("speedInfo")
    var speedInfo: List<SpeedInfo?>?,
    @SerializedName("status")
    var status: Int, // 1
    @SerializedName("video")
    var video: Video?,
    @SerializedName("caId")
    val caId: String?
)

data class SpeedConfig(
    @SerializedName("dayWorkNum")
    var dayWorkNum: Int, // 1
    @SerializedName("infoSpeedBtnTxt")
    var infoSpeedBtnTxt: String?, // 立即加速1天
    @SerializedName("tipSpeedBtnContent")
    var tipSpeedBtnContent: String?, // 您的提现预计会在6天后到账，请耐心等候。
    @SerializedName("tipSpeedBtnTxt")
    var tipSpeedBtnTxt: String?, // 立即加速
    @SerializedName("toDayIsSpeed")
    var toDayIsSpeed: Boolean, // true
    @SerializedName("toDayOverBtnTxt")
    var toDayOverBtnTxt: String?, // 今日加速已达上线
    @SerializedName("txMoney")
    var txMoney: Int, // 1
    @SerializedName("txNeedDay")
    var txNeedDay: Int, // 7
    @SerializedName("txSpeedDay")
    var txSpeedDay: Int, // 1
    @SerializedName("txjd")
    var txjd: Int // 12
)

data class SpeedInfo(
    @SerializedName("time")
    var time: String?, // 2019-1-1
    @SerializedName("title")
    var title: String?, // 观看视频
    @SerializedName("txt")
    var txt: String? // +1天
)

data class Video(
    @SerializedName("appId")
    var appId: String?, // 5000974
    @SerializedName("platformId")
    var platformId: String?, // jrtt
    @SerializedName("posId")
    var posId: String?, // 900974226
    @SerializedName("txType")
    var txType: Int // 1
) {
    fun isVideo() = txType == 2
    fun isTxTask() = txType == 1
}