package com.youloft.senior.coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.youloft.net.ApiHelper.api
import com.youloft.net.bean.MissionResult
import com.youloft.net.bean.MissionResult.DataBean
import com.youloft.net.bean.MissionResult.DataBean.MissionsBean
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
        //通知任务数据改变
        dataChangeLiveData.postValue(true)
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