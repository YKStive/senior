package com.youloft.senior.coin

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alibaba.fastjson.JSONObject
import com.youloft.senior.bean.MissionResult
import com.youloft.senior.bean.MissionResult.DataBean
import com.youloft.senior.bean.MissionResult.DataBean.MissionsBean
import com.youloft.senior.net.ApiHelper.api
import com.youloft.senior.tuia.TuiaUtil
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * @author xll
 * @date 2020/6/22 9:54
 */
class CoinManager private constructor() {
    /**
     * 数据改变监听
     */
    var dataChangeLiveData: MutableLiveData<Boolean> = MutableLiveData()


    /**
     * 获取签到信息
     *
     * @return
     */
    /**
     * 顶部签名信息
     */
    var signInfo: DataBean? = null

    var tuiaData: JSONObject? = null

    /**
     * 任务列表
     */
    val tasks: MutableList<MissionsBean> = ArrayList()
    private var mLoadSubscription: Subscription? = null

    /**
     * 监听数据发生变化
     *
     * @return
     */
    fun asDataChange(): LiveData<Boolean> {
        return dataChangeLiveData
    }

    /**
     * 加载数据
     */
    fun loadData() {
        if (mLoadSubscription != null && !mLoadSubscription!!.isUnsubscribed) {
            mLoadSubscription!!.unsubscribe()
            mLoadSubscription = null
        }
        mLoadSubscription = Observable
            .unsafeCreate { subscriber: Subscriber<in MissionResult?> ->
                try {
                    val result = api.getMissionInfo()
                    if (result?.data != null) {
                        subscriber.onNext(result)
                    }
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { }
            .onErrorResumeNext(Observable.empty())
            .onExceptionResumeNext(Observable.empty())
            .subscribe { result: MissionResult? -> parseResult(result) }
    }

    /**
     * 解析结果
     * type 1：签到数据，2：活动任务，3：新手任务 4：日常任务
     *
     * @param result 任务返回结果
     */
    fun parseResult(result: MissionResult?) {
        if (result?.data == null || result.data.isEmpty()) {
            return
        }
        tasks.clear()
        for (dataBean in result.data) {
            if (dataBean.type == 1) {
                //签到信息
                signInfo = dataBean
                continue
            }
            if (dataBean.type == 2 || dataBean.type == 3 || dataBean.type == 4) {
                //这几个类型下面是具体的任务
                if (dataBean.missions != null) {
                    tasks.addAll(dataBean.missions)
                }
            }
        }
        //加载tuia数据
        if (tuiaData == null) {
            for (i in 0 until tasks.size) {
                if (tasks[i].isTuiaTask) {
                    loadTuiaData()
                    break
                }
            }
        }
        //通知任务数据改变
        dataChangeLiveData.postValue(true)
    }

    public fun reloadTuia() {
        tuiaData = null
        loadTuiaData()
    }

    private fun loadTuiaData() {
        TuiaUtil.requestTuiaAd("332032")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable -> }
            .onErrorResumeNext(Observable.empty())
            .onErrorResumeNext(Observable.empty())
            .subscribe { jsonObject ->
                if (jsonObject?.getJSONObject("data") != null) {
                    tuiaData = jsonObject.getJSONObject("data")
                    if (tuiaData != null && !tuiaData!!.containsKey("activityUrl") || !tuiaData!!.containsKey(
                            "imageUrl"
                        )
                    ) {
                        tuiaData = null
                        return@subscribe
                    }
                    //通知任务数据改变
                    dataChangeLiveData.postValue(true)
                }
            }
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
        val instance by lazy { CoinManager() }

    }

}

/**
 * 金额显示处理
 */
fun String?.stringToInt(): String {
    try {
        if (TextUtils.isEmpty(this)) {
            return ""
        }
        val doubleValue = this?.toDouble()
        if (doubleValue == doubleValue?.toInt()?.toDouble()) {
            return doubleValue?.toInt().toString()
        }
        return this ?: ""
    } catch (e: Exception) {
        return this ?: ""
    }
}