package com.youloft.senior.ui

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.alibaba.fastjson.JSONObject
import com.youloft.core.Analytics
import com.youloft.senior.R
import com.youloft.senior.bean.MissionResult
import com.youloft.senior.cash.CashActivity
import com.youloft.senior.coin.*
import com.youloft.senior.tuia.TuiaUtil
import com.youloft.senior.tuia.TuiaWebActivity
import com.youloft.senior.ui.graphic.InviteFriendActivity
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.utils.UserManager
import com.youloft.util.ToastMaster
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.main_coin_page_layout.view.*
import kotlinx.android.synthetic.main.main_coin_page_sign_item_layout.view.*
import kotlinx.android.synthetic.main.main_coin_page_task_item_layout.view.*
import kotlinx.android.synthetic.main.main_coin_page_task_item_layout.view.item_coin
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

/**
 * @author xll
 * @date 2020/6/22 10:50
 */
internal class MainCoinPage(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.main_coin_page_layout, this)
        CoinManager.instance.loadData()
        CoinManager.instance.asDataChange().observe(context as FragmentActivity, Observer {
            refreshUI()
        })
        more.setOnClickListener {
            openOrCloseMore()
        }
        coin_more.setOnClickListener {
            if (goLogin()) {
                return@setOnClickListener
            }
            context.startActivity(Intent(context, CoinDetailActivity::class.java))
        }
        submit.setOnClickListener {
            if (goLogin()) {
                return@setOnClickListener
            }
            context.startActivity(Intent(context, CashActivity::class.java))
        }
        Analytics.reportEvent("Gold.IM")
    }

    private fun goLogin(): Boolean {
        if (!UserManager.instance.hasLogin()) {
            //登录
            LoginDialog(context as AppCompatActivity, (context as AppCompatActivity).lifecycleScope)
                .show()
            return true
        }
        return false
    }

    fun onBack(): Boolean {
        if (content_group.visibility == View.VISIBLE) {
            openOrCloseMore()
            return true
        }
        return false
    }

    var animationing: Boolean = false

    private fun openOrCloseMore() {
        if (animationing) {
            //动画中，忽略
            return
        }
        animationing = true
        if (content_group.visibility == View.VISIBLE) {
            //关闭
            isClickable = false
            val height = content_group.height
            val animation: ValueAnimator = ValueAnimator.ofInt(height)
            animation.duration = 300
            animation.addUpdateListener {
                if (it.animatedValue as Int >= height) {
                    animationing = false
                    content_group.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    content_group.visibility = View.GONE
                    more.setImageResource(R.drawable.jb_pull_down)
                    content_group_bg.setBackgroundResource(R.color.main_coin_bg_color1)
                    return@addUpdateListener
                }
                content_group.layoutParams.height = height - (it.animatedValue as Int)
                content_group.requestLayout()
            }
            animation.start()
        } else {
            //打开
            isClickable = true
            bottom_layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val height = getHeight() - top_group.height - UiUtil.dp2Px(context, 30f)
            val animation: ValueAnimator = ValueAnimator.ofInt(height)
            animation.duration = 300
            content_group.visibility = View.VISIBLE
            content_group.layoutParams.height = 0
            animation.addUpdateListener {
                if (it.animatedValue as Int >= height) {
                    animationing = false
//                    content_group.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    content_group.requestLayout()
                    more.setImageResource(R.drawable.jb_pull_up)
                    content_group_bg.setBackgroundResource(R.color.main_coin_bg_color2)
                    return@addUpdateListener
                }
                content_group.layoutParams.height = it.animatedValue as Int
                content_group.requestLayout()
            }
            animation.start()
        }
    }

    private fun refreshUI() {
        bindTopInfo();
        bindTask()
        bindSignGroup()
    }

    /**
     * 绑定顶部消息
     */
    private fun bindTopInfo() {
        //计算未完成任务的总金币
        var allCoin = 0
        val tasks = CoinManager.instance.tasks
        var maxTask: MissionResult.DataBean.MissionsBean? = null
        if (tasks.isNotEmpty()) {
            for (i in 0 until tasks.size) {
                if (tasks[i].subItems != null && tasks[i].subItems.isNotEmpty()) {
                    allCoin += tasks[i].subItems[0].coin * tasks[i].subItems[0].dayCount
                    if (maxTask == null || tasks[i].subItems[0].coin > maxTask.subItems[0].coin) {
                        maxTask = tasks[i]
                    }
                }
            }
        }
        coin_value.text = allCoin.toString()
        val signInfo: MissionResult.DataBean? = CoinManager.instance.signInfo
        if (signInfo == null) {
            coin_process.setProcess(0, 20000)
            my_coin_value.text = "0"
            my_cash_value.text = "0元"
        } else {
            coin_process.setProcess(signInfo.todayCoin, allCoin)
            my_coin_value.text = signInfo.gold.toString()
            my_cash_value.text = "约${signInfo.cash}元"
        }
        next_coin_name.text = if (maxTask == null) "暂无" else maxTask.content
    }

    /**
     * 将保留两位小数
     *
     * @param cash
     * @return
     */
    private fun formatMoney(cash: Int): String? {
        var bigDecimal = BigDecimal(cash)
        bigDecimal.setScale(2)
        bigDecimal = bigDecimal.divide(BigDecimal(100))
        return DecimalFormat("0.00").format(bigDecimal)
    }

    private fun bindTask() {
        val tasks = CoinManager.instance.tasks
        if (tasks.isEmpty()) {
            task_layout.removeAllViews()
            return
        }
        for (i in 0 until tasks.size) {
            var holder: ViewHolder
            if (task_layout.childCount > i) {
                holder = task_layout.getChildAt(i).tag as ViewHolder
            } else {
                holder = ViewHolder(context)
                holder.itemView.tag = holder
                task_layout.addView(holder.itemView)
            }
            holder.itemView.visibility = View.VISIBLE
            holder.bindItem(tasks[i])
        }

        for (i in tasks.size until task_layout.childCount) {
            task_layout.getChildAt(i).visibility = View.GONE
        }
    }

    /**
     * 绑定签到数据
     */
    private fun bindSignGroup() {
        val signInfo: MissionResult.DataBean? = CoinManager.instance.signInfo ?: return
        if (signInfo!!.coin_signin_contents == null || signInfo.coin_signin_contents.isEmpty()) {
            return
        }
        var continued = signInfo.continued % signInfo.coin_signin_contents.size
        for (i in 0 until signInfo.coin_signin_contents.size) {
            var holder: SignViewHolder
            if (sign_group.childCount > i) {
                holder = sign_group.getChildAt(i).tag as SignViewHolder
            } else {
                holder = SignViewHolder(context)
                holder.itemView.tag = holder
                sign_group.addView(holder.itemView)
            }
            holder.itemView.visibility = View.VISIBLE
            holder.bindItem(
                signInfo.coin_signin_contents[i],
                i,
                continued,
                signInfo.status == 1
            )
        }

        for (i in signInfo.coin_signin_contents.size until sign_group.childCount) {
            sign_group.getChildAt(i).visibility = View.GONE
        }
    }

    inner class SignViewHolder(ctx: Context) {
        val itemView: View =
            LayoutInflater.from(ctx).inflate(R.layout.main_coin_page_sign_item_layout, null, false)

        fun bindItem(value: Int, position: Int, con: Int, toadySign: Boolean) {
            itemView.item_day.text = "第${position + 1}天"

            //修改整当前item状态
            var state = 0
            if (toadySign && position == (con - 1)) {
                //今天已签到
                val signInfo: MissionResult.DataBean? = CoinManager.instance.signInfo
                if (signInfo != null) {
                    //已签到
                    if (signInfo.coinSigninContentsDoublecode != null && signInfo.coinSigninContentsDoublecode.size > signInfo.continued) {
                        val doubleCode =
                            signInfo.coinSigninContentsDoublecode[signInfo.continued - 1]
                        if (!TextUtils.isEmpty(doubleCode) && !TaskManager.instance.isComplete(
                                doubleCode
                            )
                        ) {
                            //有双倍
                            state = 3;
                        }
                    }
                }
            } else if (!toadySign && position == con) {
                //当前是今天，但是今天么有签到的情况
                state = 1;
            } else if (position >= con) {
                //未签到的天
                state = 2
            } else {
                //已签到
                state = 0
            }

            when {
                state == 0 -> {
                    itemView.setOnClickListener(null)
                    itemView.isSelected = true
                    itemView.item_icon.setImageResource(R.drawable.qd_jbcheck_icon_sign)
                    itemView.item_coin.text = "已签"
                    itemView.item_coin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                }
                state == 1 -> {
                    itemView.isSelected = true
                    itemView.setOnClickListener {
                        if (goLogin()) {
                            return@setOnClickListener
                        }
                        TaskManager.instance.sign(itemView.context)
                    }
                    itemView.item_icon.setImageResource(R.drawable.qd_jb_sel_icon)
                    itemView.item_coin.text = "+${value}"
                    itemView.item_coin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
                }
                state == 2 -> {
                    itemView.setOnClickListener(null)
                    itemView.isSelected = false
                    itemView.item_icon.setImageResource(R.drawable.qd_jb_unsel_icon)
                    itemView.item_coin.text = "+${value}"
                    itemView.item_coin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
                }
                state == 3 -> {
                    itemView.isSelected = true
                    itemView.setOnClickListener {
                        TaskManager.instance.sign(itemView.context)
                    }
                    itemView.item_icon.setImageResource(R.drawable.js_buttun_video_sign)
                    itemView.item_coin.text = "可翻倍"
                    itemView.item_coin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                }
            }
        }
    }


    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == 10202 && resultCode == Activity.RESULT_OK && data != null) {
            if (TuiaUtil.getTuiaBean() == null) {
                return
            }
            //tuia读取完成，去提交任务
            val tuiaData = data.getStringExtra("tuia_data")
            if (!TextUtils.isEmpty(tuiaData)) {
                //完成任务
                TaskManager.instance.completeTask(TuiaUtil.getTuiaBean(), context, null, tuiaData)
            }
        }
    }

    inner class ViewHolder(ctx: Context) {
        val itemView: View =
            LayoutInflater.from(ctx).inflate(R.layout.main_coin_page_task_item_layout, null, false)

        init {
            itemView.item_button.setOnClickListener {
                if (goLogin()) {
                    return@setOnClickListener
                }
                if (bean == null) {
                    return@setOnClickListener
                }
                if (bean!!.hasDone()) {
                    //已经完成了,判断是否有双倍，且没完成的情况
                    if (bean!!.subItems != null && bean!!.subItems.isNotEmpty() && !TextUtils.isEmpty(
                            bean!!.subItems[0].doubleCode
                        )
                    ) {
                        //有双倍
                        if (!TaskManager.instance.isComplete(bean!!.subItems[0].doubleCode)) {
                            val mode = TaskManager.instance.createDouble(bean!!)
                            if (mode != null) {
                                TaskManager.instance.completeDoubleTask(itemView.context, mode)
                            }
                        }
                    }
                    return@setOnClickListener
                }
                if (bean!!.isTuiaTask) {
                    if (bean!!.tuiaData == null) {
                        return@setOnClickListener
                    }
                    TuiaUtil.setTuiaBean(bean)
                    TuiaUtil.reportUrl(bean!!.tuiaData.getString("reportClickUrl"))
                    TuiaWebActivity.start(
                        itemView.context as Activity?,
                        TuiaUtil.getActivityUrl(bean!!.tuiaData.getString("activityUrl"))
                    )
                    CoinManager.instance.reloadTuia()
                    return@setOnClickListener
                }
                //如果么有登录，跳转登录界面
                if (bean!!.isRewardTask) {
                    //激励视频任务
                    if (bean!!.subItems == null || bean!!.subItems.isEmpty()) {
                        return@setOnClickListener
                    }
                    if (TaskManager.instance.getRemainTimeFor(bean!!) > 0) {
                        //倒计时还没有结束
                        return@setOnClickListener
                    }
                    val uuid = UUID.randomUUID().toString()
                    val extra = JSONObject()
                    extra["uuid"] = uuid
                    extra["code"] = bean!!.subItems[0].code
                    TTRewardManager.requestReword(
                        ctx as Activity,
                        bean!!.subItems[0].posId,
                        object : RewardListener() {
                            override fun onRewardResult(
                                isSuccess: Boolean,
                                reward: Boolean,
                                args: JSONObject?
                            ) {
                                if (isSuccess && reward) {
                                    TaskManager.instance.completeTask(
                                        bean!!.subItems[0].code,
                                        itemView.context, null, null, null, null, null, uuid
                                    )
                                } else if (!isSuccess) {
                                    ToastMaster.showShortToast(ctx, "这个任务看起来好像是迷路了,请稍候再试")
                                }
                            }
                        },
                        extra
                    )
                    return@setOnClickListener
                }
                if (bean!!.isActivity) {
                    //活动任务
                    return@setOnClickListener
                }
                if (bean!!.isEmoj) {
                    //创建表情任务
                    TaskManager.instance.completeTask(bean!!, itemView.context)
                    return@setOnClickListener
                }
                if (bean!!.isImageText) {
                    //创建图文任务
                    TaskManager.instance.completeTask(bean!!, itemView.context)
                    return@setOnClickListener
                }
                if (bean!!.isPhoto) {
                    //创建相册的任务
                    TaskManager.instance.completeTask(bean!!, itemView.context)
                    return@setOnClickListener
                }
                if (bean!!.isZanEmoj) {
                    //赞表情的任务
                    TaskManager.instance.completeTask(bean!!, itemView.context)
                    return@setOnClickListener
                }
                if (bean!!.isZanImageText) {
                    //赞图文的任务
                    TaskManager.instance.completeTask(bean!!, itemView.context)
                    return@setOnClickListener
                }
                if (bean!!.isZanPhoto) {
                    //赞相册的任务
                    TaskManager.instance.completeTask(bean!!, itemView.context)
                    return@setOnClickListener
                }
                if (bean!!.isInvite) {
                    //邀请好友
                    InviteFriendActivity.start(context as FragmentActivity)
                    return@setOnClickListener
                }
                if (bean!!.isWriteCode) {
                    //填写邀请码
                    InviteDialog(ctx).show()
                    return@setOnClickListener
                }
            }
        }

        var bean: MissionResult.DataBean.MissionsBean? = null

        fun bindItem(bean: MissionResult.DataBean.MissionsBean) {
            MissionCountDownTimer.cancelTimer(itemView.item_button)
            this.bean = bean
            itemView.item_title.text = bean.content
            if (bean.subItems == null || bean.subItems.isEmpty()) {
                return
            }
            if (bean.isTuiaTask) {
                if (CoinManager.instance.tuiaData == null) {
                    itemView.visibility = View.GONE
                } else {
                    itemView.visibility = View.VISIBLE
                    val report: Boolean =
                        CoinManager.instance.tuiaData!!.getBooleanValue("is_report_value")
                    if (!report) {
                        CoinManager.instance.tuiaData!!["is_report_value"] = true
                        TuiaUtil.reportUrl(CoinManager.instance.tuiaData!!.getString("reportExposureUrl"))
                    }
                }
            }
            itemView.item_content.text = bean.subItems[0].content
            itemView.item_coin.text = "+${bean.subItems[0].coin}"
            itemView.item_button.text =
                if (bean.hasDone()) "已完成" else if (TextUtils.isEmpty(bean.button)) "立即完成" else bean.button
            if (bean.isRewardTask && !bean.hasDone()) {
                //判定是否有 iterval
                MissionCountDownTimer.updateCountDown(
                    itemView.item_button,
                    TaskManager.instance.getRemainTimeFor(bean)
                )
            }
            if (bean.hasDone()) {
                if (!TextUtils.isEmpty(bean.subItems[0].doubleCode)) {
                    //有双倍
                    if (!TaskManager.instance.isComplete(bean.subItems[0].doubleCode)) {
                        itemView.item_button.text = "立即播放"
                    }
                }
            } else {

            }
        }

    }
}