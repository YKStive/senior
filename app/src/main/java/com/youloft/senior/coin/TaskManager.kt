package com.youloft.senior.coin

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.JsonObject
import com.youloft.net.ApiHelper
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
            .subscribe { result: JsonObject? -> parseResult(result) }
    }

    private fun parseResult(data: JsonObject?) {
        if (data == null) {
            return
        }
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
        //成功，刷新数据
        CoinManager.instance.loadData()
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