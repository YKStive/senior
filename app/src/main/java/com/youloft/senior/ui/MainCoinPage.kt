package com.youloft.senior.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.youloft.net.bean.MissionResult
import com.youloft.senior.R
import com.youloft.senior.coin.CoinManager
import com.youloft.senior.coin.TaskManager
import kotlinx.android.synthetic.main.main_coin_page_layout.view.*
import kotlinx.android.synthetic.main.main_coin_page_sign_item_layout.view.*
import kotlinx.android.synthetic.main.main_coin_page_task_item_layout.view.*
import kotlinx.android.synthetic.main.main_coin_page_task_item_layout.view.item_coin
import java.math.BigDecimal
import java.text.DecimalFormat

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
                    allCoin += tasks[i].subItems[0].coin
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
            my_cash_value.text = "约${formatMoney(signInfo.cash)}元"
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
     * 签到
     */
    private fun sign() {
        val signInfo: MissionResult.DataBean? = CoinManager.instance.signInfo ?: return
        if (signInfo == null || signInfo.status == 1) {
            //已签到
            return
        }
        TaskManager.instance.completeTask("signin", context)
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
        sign_button.text = if (signInfo.status == 1) "已签到" else "立即签到"
        sign_button.setOnClickListener {
            sign()
        }
    }

    class SignViewHolder(ctx: Context) {
        val itemView: View =
            LayoutInflater.from(ctx).inflate(R.layout.main_coin_page_sign_item_layout, null, false)

        fun bindItem(value: Int, position: Int, con: Int, toadySign: Boolean) {
            itemView.item_day.text = "第${position + 1}天"
            itemView.item_coin.text = "+${value}"

            //修改整当前item状态
            var state = 0
            if (!toadySign && position == con) {
                //当前是今天，但是今天么有签到的情况
                state = 1;
                itemView.isSelected = true
            } else if (position >= con) {
                //为签到的天
                state = 2
                itemView.isSelected = false
            }
        }
    }


    class ViewHolder(ctx: Context) {
        val itemView: View =
            LayoutInflater.from(ctx).inflate(R.layout.main_coin_page_task_item_layout, null, false)

        fun bindItem(bean: MissionResult.DataBean.MissionsBean) {
            itemView.item_title.text = bean.content
            itemView.item_content.text = bean.subContent
            if (bean.subItems != null && bean.subItems.isNotEmpty()) {
                itemView.item_coin.text = "+${bean.subItems[0].coin}"
            }
            itemView.item_button.text = if (bean.hasDone()) "已完成" else bean.button
            itemView.item_button.setOnClickListener {
                if (bean.hasDone()) {
                    //已经完成了
                    return@setOnClickListener
                }
                if (bean.isRewardTask) {
                    //激励视频任务
                    return@setOnClickListener
                }

            }
        }

    }
}