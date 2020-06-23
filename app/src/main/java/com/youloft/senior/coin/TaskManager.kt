package com.youloft.senior.coin

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.google.gson.JsonObject
import com.youloft.net.ApiHelper
import com.youloft.net.bean.MissionResult.DataBean.MissionsBean
import com.youloft.senior.base.App
import com.youloft.senior.bean.DoubleBean
import com.youloft.util.MD5
import com.youloft.util.ToastMaster
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * @author xll
 * @date 2020/6/22 15:41
 */
internal class TaskManager {
    /**
     * 完成任务
     */
    public fun completeTask(
        bean: MissionsBean,
        context: Context,
        otherinfo: String? = null, extData: String? = null
    ) {
        if (bean.subItems == null || bean.subItems.isEmpty()) {
            return
        }
        completeTask(bean.subItems[0].code, context, createDouble(bean), otherinfo, extData)
    }

    fun createDouble(bean: MissionsBean): DoubleBean? {
        if (bean.subItems == null || bean.subItems.isEmpty()) {
            return null
        }
        val result = DoubleBean()
        result.doubleCode = bean.subItems[0].doubleCode
        result.posid = bean.subItems[0].posId
        result.appid = bean.subItems[0].appId
        return result
    }

    fun sign(ctx: Context) {
        val info = CoinManager.instance.signInfo ?: return
        if (info.status == 1) {
            //已签到
            return
        }
        val doubleBean = DoubleBean()
        if (info.coinSigninContentsDoublecode != null && info.coinSigninContentsDoublecode.size > info.continued) {
            doubleBean.doubleCode = info.coinSigninContentsDoublecode[info.continued]
            doubleBean.posid = info.posId
            doubleBean.appid = info.appId
        }
        completeTask("signin", ctx, doubleBean)
    }

    /**
     * 完成任务
     */
    public fun completeTask(
        code: String,
        context: Context,
        doubleMode: DoubleBean? = null,
        otherinfo: String? = null, extData: String? = null
    ) {
        if (context != null) {

        }
        Observable
            .unsafeCreate { subscriber: Subscriber<in JsonObject?> ->
                try {
                    val time: Long = System.currentTimeMillis()
                    val sn: String =
                        MD5.encode("Youloft_Android" + time + "D0513B7CEF494E82AEAFDFF7B2183ECF")
                    val result =
                        ApiHelper.api.completeTask(code, time.toString(), sn, otherinfo, extData)
                    subscriber.onNext(result)
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { }
            .onErrorResumeNext(Observable.empty())
            .onExceptionResumeNext(Observable.empty())
            .subscribe { result: JsonObject? -> parseResult(context, result, code, doubleMode) }
    }

    private fun parseResult(
        ctx: Context,
        result: JsonObject?,
        code: String,
        doubleMode: DoubleBean?
    ) {
        if (result == null) {
            return
        }
        if (!result.has("data")) {
            return
        }
        recordLastCompletedTime(code)
        val data: JsonObject = result.getAsJsonObject("data") ?: return
        if (!data.has("coin") || data.get("coin").asInt <= 0) {
            if (data.has("msg") && !TextUtils.isEmpty(data.get("msg").asString)) {
                ToastMaster.showShortToast(
                    App.instance(),
                    data.get("msg").asString,
                    Toast.LENGTH_SHORT
                )
                return
            }
            ToastMaster.showShortToast(App.instance(), "网络异常", Toast.LENGTH_SHORT)
            return
        }
        recordLastCompletedTime(code)
        handleDoubleMode(ctx, doubleMode)
        //成功，刷新数据
        CoinManager.instance.loadData()

    }

    fun handleDoubleMode(ctx: Context, doubleMode: DoubleBean?) {
        if (doubleMode == null || !TextUtils.isEmpty(doubleMode.doubleCode)) {
            return
        }
        if (!TextUtils.isEmpty(
                doubleMode.posid
            )
        ) {
            //双倍任务完成后保存值
            saveComplete(doubleMode.doubleCode!!, true)
            return
        }
        //去完成双倍任务
        //应该先弹出对话框
        completeDoubleTask(ctx, doubleMode)
    }

    /**
     * 去完成视频双倍，并提交双倍到服务器
     */
    fun completeDoubleTask(ctx: Context, doubleMode: DoubleBean) {
        TTRewardManager.requestReword(
            ctx as Activity,
            doubleMode.posid,
            object : RewardListener() {
                override fun onRewardResult(
                    isSuccess: Boolean,
                    reward: Boolean,
                    args: JSONObject?
                ) {
                    if (isSuccess && reward) {
                        //双倍提交任务
                        val doubleBean1 = DoubleBean()
                        doubleBean1.doubleCode = doubleMode.doubleCode
                        completeTask(
                            doubleMode.doubleCode!!,
                            ctx, doubleBean1
                        )
                    } else if (!isSuccess) {
                        ToastMaster.showShortToast(ctx, "这个任务看起来好像是迷路了,请稍候再试")
                    }
                }
            },
            null
        )
    }

    /**
     * 获取余下时间
     *
     * @param missionsBean
     * @return
     */
    fun getRemainTimeFor(missionsBean: MissionsBean): Long {
        if (missionsBean.subItems == null || missionsBean.subItems.isEmpty()) {
            return 0
        }
        val subItemsBean = missionsBean.subItems[0] ?: return 0
        if (subItemsBean.interval <= 0) {
            return 0
        }
        val endTime: Long = getMissionSp().getLong(subItemsBean.code, 0)
        val remainTime =
            subItemsBean.interval * 1000 - (System.currentTimeMillis() - endTime)
        return if (remainTime < 0) {
            0
        } else remainTime
    }


    /**
     * 记录最后完成时间
     *
     * @param code
     * @return
     */
    fun recordLastCompletedTime(code: String?) {
        getMissionSp().edit()
            .putLong(code, System.currentTimeMillis()).apply()
    }

    /**
     * 获取 Mission 存储
     *
     * @return
     */
    fun getMissionSp(): SharedPreferences {
        return App.instance()
            .getSharedPreferences("mission_task_time", Context.MODE_PRIVATE)
    }

    fun getTaskSp(): SharedPreferences {
        val sp = App.instance()
            .getSharedPreferences("task_double_sp", Context.MODE_PRIVATE)
        val calendar: Calendar = Calendar.getInstance()
        val lastCalendar: Calendar = Calendar.getInstance()
        lastCalendar.timeInMillis = sp.getLong("last_time", 0)
        if (calendar.get(Calendar.YEAR) != lastCalendar.get(Calendar.YEAR)
            || calendar.get(Calendar.MONTH) != lastCalendar.get(Calendar.MONTH)
            || calendar.get(Calendar.DAY_OF_MONTH) != lastCalendar.get(Calendar.DAY_OF_MONTH)
        ) {
            sp.edit().clear().putLong("last_time", System.currentTimeMillis()).apply()
        }
        return sp
    }

    fun isComplete(doubleCode: String): Boolean {
        return getTaskSp().getBoolean(doubleCode, false)
    }

    fun saveComplete(doubleCode: String, value: Boolean) {
        getTaskSp().edit().putBoolean(doubleCode, value).apply()
    }

    companion object {
        /**
         * 获取实例
         *
         * @return
         */
        /**
         * 实例
         */
        val instance by lazy { TaskManager() }

    }
}