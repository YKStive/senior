package com.youloft.senior.coin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.youloft.senior.base.App
import com.youloft.senior.bean.DoubleBean
import com.youloft.senior.bean.MissionResult.DataBean.MissionsBean
import com.youloft.senior.cash.CashActivity
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.UserManager
import com.youloft.senior.widgt.ProgressHUD
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
            if (info.coinSigninContentsDoublecode != null && info.coinSigninContentsDoublecode.size > info.continued) {
                val doubleCode = info.coinSigninContentsDoublecode[info.continued - 1]
                if (!TextUtils.isEmpty(doubleCode) && !isComplete(doubleCode)) {
                    //有双倍
                    val doubleBean = DoubleBean()
                    doubleBean.doubleCode = doubleCode
                    doubleBean.posid = info.posId
                    doubleBean.appid = info.appId
                    completeDoubleTask(ctx, doubleBean)
                }
            }
            return
        }
        val doubleBean = DoubleBean()
        if (info.coinSigninContentsDoublecode != null && info.coinSigninContentsDoublecode.size > info.continued) {
            doubleBean.doubleCode = info.coinSigninContentsDoublecode[info.continued]
            doubleBean.posid = info.posId
            doubleBean.appid = info.appId
        }
        completeTask(info.code, ctx, doubleBean)
    }

    var completeing = false

    var progressHUD: ProgressHUD? = null
    fun showProgress(ctx: Context) {
        if (progressHUD != null) {
            progressHUD!!.dismiss()
        }
        progressHUD = ProgressHUD.show(ctx, "提交中")
    }

    fun hideProgress() {
        if (progressHUD != null) {
            progressHUD!!.dismiss()
        }
    }

    /**
     * 完成任务
     */
    public fun completeTask(
        code: String,
        context: Context,
        doubleMode: DoubleBean? = null,
        otherinfo: String? = null, extData: String? = null, success: (() -> Unit)? = null
        , failed: (() -> Unit)? = null, jlspOrderId: String? = null
    ) {
        if (completeing) {
            return
        }
        showProgress(context)
        completeing = true
        Observable
            .unsafeCreate { subscriber: Subscriber<in JSONObject?> ->
                try {
                    val time: Long = System.currentTimeMillis()
                    val sn: String =
                        MD5.encode("Youloft_Android" + time + "D0513B7CEF494E82AEAFDFF7B2183ECF")
                    val result =
                        ApiHelper.api.completeTask(
                            code,
                            time.toString(),
                            sn,
                            otherinfo,
                            extData,
                            jlspOrderId
                        )
                    subscriber.onNext(result)
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                ToastMaster.showLongToast(context, "网络异常")
                failed?.invoke()
                completeing = false
                hideProgress()
            }
            .onErrorResumeNext(Observable.empty())
            .onExceptionResumeNext(Observable.empty())
            .subscribe { result: JSONObject? ->
                hideProgress()
                parseResult(
                    context,
                    result,
                    code,
                    doubleMode,
                    success,
                    failed
                )
            }
    }

    private fun parseResult(
        ctx: Context,
        result: JSONObject?,
        code: String,
        doubleMode: DoubleBean?, success: (() -> Unit)? = null
        , failed: (() -> Unit)? = null
    ) {
        completeing = false
        if (result == null) {
            ToastMaster.showLongToast(ctx, "网络异常")
            failed?.invoke()
            return
        }
        if (!result.containsKey("data")) {
            ToastMaster.showLongToast(ctx, "网络异常")
            failed?.invoke()
            return
        }
        val data: JSONObject = result.getJSONObject("data") ?: return
        if (!data.containsKey("coin") || data.getIntValue("coin") <= 0) {
            failed?.invoke()
            if (data.containsKey("msg") && !TextUtils.isEmpty(data.getString("msg"))) {
                ToastMaster.showShortToast(
                    App.instance(),
                    data.getString("msg"),
                    Toast.LENGTH_SHORT
                )
                return
            }
            ToastMaster.showShortToast(App.instance(), "网络异常", Toast.LENGTH_SHORT)
            return
        }
        success?.invoke()
        recordLastCompletedTime(code)
        handleDoubleMode(ctx, doubleMode, result)
        //成功，刷新数据
        CoinManager.instance.loadData()

    }

    fun handleDoubleMode(ctx: Context, doubleMode: DoubleBean?, result: JSONObject) {
        val data: JSONObject = result.getJSONObject("data") ?: return
        if (doubleMode == null || TextUtils.isEmpty(doubleMode.doubleCode)) {
            //没有双倍
            //弹普通获取弹窗
            CoinTipsDialog(ctx, "恭喜获得", "", data.getIntValue("coin"), null, null)
                .bindCoinCash(data.getIntValue("useCoin"), data.getString("useCash")).show()
            return
        }
        if (TextUtils.isEmpty(
                doubleMode.posid
            )
        ) {
            if (doubleMode.cash) {
                //三倍后的结果
                CoinTipsDialog(
                    ctx,
                    "恭喜获得",
                    "可立即提现至微信",
                    data.getIntValue("coin"),
                    data.getString("cash").stringToInt(),
                    "立即提现"
                )
                    .setButtonListener {
                        //跳转至提现
                        ctx.startActivity(Intent(ctx, CashActivity::class.java))
                    }.show()
                saveComplete(doubleMode.doubleCode!!, true)
                return
            }
            //双倍任务完成后保存值
            CoinTipsDialog(ctx, "获得翻倍奖励", "", data.getIntValue("coin"), null, null)
                .setButtonListener {

                }
                .bindCoinCash(data.getIntValue("useCoin"), data.getString("useCash"))
                .show()
            saveComplete(doubleMode.doubleCode!!, true)
            return
        }
        if (data.getBooleanValue("isCoinThree")) {
            //翻三倍
            CoinTipsDialog(
                ctx,
                "恭喜获得",
                "可立即提现至微信",
                data.getIntValue("coin"),
                data.getString("cash").stringToInt(),
                "cash-double"
            )
                .setButtonListener {
                    doubleMode.cash = true
                    completeDoubleTask(ctx, doubleMode)
                }.show()
            return
        }
        if (data.getBooleanValue("isCoinDouble")) {
            //翻两倍
            CoinTipsDialog(ctx, "恭喜获得", "", data.getIntValue("coin"), null, "翻倍奖励")
                .setButtonListener {
                    completeDoubleTask(ctx, doubleMode)
                }
                .bindCoinCash(data.getIntValue("useCoin"), data.getString("useCash"))
                .show()
            return
        }
        //默认有两倍的code参数，翻两倍
        CoinTipsDialog(ctx, "恭喜获得", "", data.getIntValue("coin"), null, "翻倍奖励")
            .setButtonListener {
                completeDoubleTask(ctx, doubleMode)
            }
            .bindCoinCash(data.getIntValue("useCoin"), data.getString("useCash"))
            .show()
    }

    /**
     * 去完成视频双倍，并提交双倍到服务器
     */
    fun completeDoubleTask(ctx: Context, doubleMode: DoubleBean) {
        val uuid = UUID.randomUUID().toString()
        val extra = JSONObject()
        extra["uuid"] = uuid
        extra["code"] = doubleMode.doubleCode
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
                        doubleBean1.cash = doubleMode.cash
                        completeTask(
                            doubleMode.doubleCode!!,
                            ctx, doubleBean1, null, null, null, null, uuid
                        )
                    } else if (!isSuccess) {
                        ToastMaster.showShortToast(ctx, "这个任务看起来好像是迷路了,请稍候再试")
                    }
                }
            },
            extra
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


    /**
     * 完成创建相册任务
     */
    fun completeCreatePhoto(ctx: Context) {
        completeTaskByKey(ctx, "photo")
    }

    /**
     * 完成创建表情任务
     */
    fun completeCreateEmoj(ctx: Context) {
        completeTaskByKey(ctx, "emoj")
    }

    /**
     * 完成创建图文任务
     */
    fun completeCreateImageText(ctx: Context) {
        completeTaskByKey(ctx, "imagetext")
    }

    /**
     * 完成赞表情任务
     */
    fun completeZanEmoj(ctx: Context) {
        completeTaskByKey(ctx, "zan_emoj")
    }

    /**
     * 完成赞图文任务
     */
    fun completeZanImageText(ctx: Context) {
        completeTaskByKey(ctx, "zan_imagetext")
    }

    /**
     * 完成赞相册任务
     */
    fun completeZanPhoto(ctx: Context) {
        completeTaskByKey(ctx, "zan_photo")
    }

    /**
     * 通过任务code关键字去完成对应任务
     */
    private fun completeTaskByKey(ctx: Context, key: String) {
        if (goLogin(ctx)) {
            return
        }
        val tasks: MutableList<MissionsBean> = CoinManager.instance.tasks
        if (tasks.isEmpty()) {
            return
        }
        for (item in tasks) {
            if (item.isKeyTask(key)) {
                completeTask(item, ctx)
                return
            }
        }
    }

    private fun goLogin(ctx: Context): Boolean {
        if (!UserManager.instance.hasLogin()) {
            return true
        }
        return false
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