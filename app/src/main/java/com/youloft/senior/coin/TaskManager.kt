package com.youloft.senior.coin

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.JsonObject
import com.youloft.net.ApiHelper
import com.youloft.net.bean.MissionResult.DataBean.MissionsBean
import com.youloft.senior.base.App
import com.youloft.util.MD5
import com.youloft.util.ToastMaster
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author xll
 * @date 2020/6/22 15:41
 */
internal class TaskManager {

    /**
     * 完成任务
     */
    public fun completeTask(
        code: String,
        context: Context? = null,
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
            .subscribe { result: JsonObject? -> parseResult(result, code) }
    }

    private fun parseResult(result: JsonObject?, code: String) {
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
        //成功，刷新数据
        CoinManager.instance.loadData()
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