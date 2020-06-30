package com.youloft.senior.cash

import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSONObject
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.coin.RewardListener
import com.youloft.senior.coin.TTRewardManager
import com.youloft.senior.coin.stringToInt
import com.youloft.senior.net.ApiHelper
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.money_apply_progress.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MoneyApplyProgressActivity : BaseActivity() {
    private var lastCashData: JSONObject? = null
    private var caid: String? = ""

    override fun getLayoutResId(): Int {
        return R.layout.money_apply_progress
    }

    override fun initView() {
        ic_back.setOnClickListener { finish() }
        speed_button_text.setOnClickListener {
            if (lastCashData == null || lastCashData!!.getJSONObject("video") == null) {
                return@setOnClickListener
            }
            val speedConfig = lastCashData!!.getJSONObject("speedConfig")
            //当天可以加速
            if (speedConfig == null || !speedConfig.getBooleanValue("toDayIsSpeed")) {
                ToastMaster.showLongToast(this@MoneyApplyProgressActivity, "今天加速用完了，明天再试")
                return@setOnClickListener
            }
            val video = lastCashData!!.getJSONObject("video")
            val uuid = UUID.randomUUID().toString()
            val extra = JSONObject()
            extra["uuid"] = uuid
            extra["code"] = lastCashData!!.getIntValue("caId").toString()
            TTRewardManager.requestReword(
                this,
                video.getString("posId"),
                object : RewardListener() {
                    override fun onRewardResult(
                        isSuccess: Boolean,
                        reward: Boolean,
                        args: JSONObject?
                    ) {
                        initData()
                    }
                },
                extra
            )
        }
        refresh_view.showLoading()
        refresh_view.bindRefreshCallBack {
            initData()
        }
    }

    override fun initData() {
        if (TextUtils.isEmpty(caid) && intent != null) {
            caid = intent.getStringExtra("caid")
        }
        if (TextUtils.isEmpty(caid)) {
            ToastMaster.showLongToast(this, "错误的订单号")
            finish()
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            val lastCash = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    ApiHelper.api.getCashRecord(caid!!)
                }.getOrNull()
            }
//            val lastCash = JSONObject.parseObject(dd)
            lastCashData = if (lastCash != null && lastCash.getBooleanValue("success")) {
                lastCash.getJSONObject("data")
            } else {
                null
            }
            bindUI()
        }
    }

    private fun loadErr() {
        refresh_view.showErr()
    }

    private fun bindUI() {
        if (lastCashData == null) {
            //加载失败页面
            loadErr()
            return
        }
        refresh_view.showSuccess()
        speed_history.refreshData(lastCashData!!.getJSONArray("speedInfo"))
        val speedConfig = lastCashData!!.getJSONObject("speedConfig")
        if (speedConfig != null) {
            speed_button.text = speedConfig.getString("infoSpeedBtnTxt")
            if (speedConfig.getBooleanValue("toDayIsSpeed")) {
                //当天可以加速
            } else {
                //当天不可以加速
            }
            cash_value.text = speedConfig.getString("txMoney").stringToInt()
        }
        when {
            //异常
            lastCashData!!.getIntValue("status") == -1 -> {
                setActionState(-1, lastCashData!!.getString("msg"))
            }
            //提现完成
            speedConfig?.getIntValue("txjd") ?: 0 >= speedConfig?.getIntValue("txNeedDay") ?: 0 -> {
                setActionState(1, "")
            }
            //当天可以加速
            speedConfig?.getBooleanValue("toDayIsSpeed") == true -> {
                setActionState(0, "")
            }
            //当天加速完成
            else -> {
                setActionState(2, "")
            }
        }
        animation_view.setTipTexts(
            speedConfig?.getString("tipSpeedBtnContent").orEmpty(),
            speedConfig?.getString("tipSpeedBtnContent").orEmpty()
        )
        animation_view.setValue(
            speedConfig?.getIntValue("txjd")
                ?: 0, speedConfig?.getIntValue("txNeedDay") ?: 7
        )
    }

    /**
     * 设置 Action 状态
     */
    private fun setActionState(state: Int, message: String?) {
        when (state) {
            0 -> {
                //正常态
//                progressAction.background.alpha = 255
                speed_button_text.visibility = View.VISIBLE
                err_text_group.visibility = View.GONE
            }
            1 -> {
                //加速完成
                speed_button_text.visibility = View.VISIBLE
                err_text_group.visibility = View.GONE
            }
            2 -> {
                speed_button_text.visibility = View.VISIBLE
                err_text_group.visibility = View.GONE
            }
            -1 -> {
                //问题状态
                speed_button_text.visibility = View.GONE
                err_text_group.visibility = View.VISIBLE
                err_text.text = message ?: "提现异常，请联系客服处理"
            }
        }
    }

}