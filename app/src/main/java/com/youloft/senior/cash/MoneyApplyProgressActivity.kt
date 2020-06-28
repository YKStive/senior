package com.youloft.senior.cash

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSONObject
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import kotlinx.android.synthetic.main.money_apply_progress.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoneyApplyProgressActivity : BaseActivity() {
    val dd = "{\n" +
            "    \"data\":{\n" +
            "        \"isSpeed\":true,//是否可以加速\n" +
            "        \"speedConfig\":{//加速配置相关\n" +
            "            \"txNeedDay\":7,//体现需要多少天到账\n" +
            "            \"txSpeedDay\":1,//每次加速多少天\n" +
            "            \"dayWorkNum\":1,//每天可以加速多少次\n" +
            "            \"txMoney\":1,//体现金额\n" +
            "            \"tipSpeedBtnTxt\":\"立即加速\",//弹窗加速按钮文字\n" +
            "            \"infoSpeedBtnTxt\":\"立即加速1天\",//详情加速按钮文字\n" +
            "            \"tipSpeedBtnContent\":\"您的提现预计\",//弹窗加速文案\n" +
            "            \"txjd\": 6,//提现进度\n" +
            "            \"toDayOverBtnTxt\": \"今日加速已达上线\",//当天提现加速完后按钮提示文本\n" +
            "            \"toDayIsSpeed\": true,//当天是否可以加速\n" +
            "        },\n" +
            "        \"video\":{//视频相关配置\n" +
            "            \"txType\":1,\n" +
            "            \"platformId\":\"jrtt\",\n" +
            "            \"posId\":\"900974226\",\n" +
            "            \"appId\":\"5000974\"\n" +
            "        },\n" +
            "        \"speedInfo\":[//加速记录\n" +
            "            {\n" +
            "                \"title\":\"观看视频\",\n" +
            "                \"time\":\"2019-1-1\",//yyyy-MM-dd HH:mm:sss\n" +
            "                \"txt\":\"+1天\"\n" +
            "            }\n" +
            "        ],\n" +
            "\t\t\"status\":1,//-1，异常 0,进行中 1，已打款\n" +
            "\t\t\"caId\":11,\n" +
            "\t\t\"cashType\":0,//0新用户专享 1普通提现\n" +
            "\t\t\"msg\":\"\"//相关提醒文本\n" +
            "    }\n" +
            "}"
    private var lastCashData: JSONObject? = null
    private val adapter: ApplyProgressAdapter by lazy {
        ApplyProgressAdapter()
    }

    override fun getLayoutResId(): Int {
        return R.layout.money_apply_progress
    }

    override fun initView() {
        speed_history.layoutManager = LinearLayoutManager(this)
        speed_history.itemAnimator = null
        speed_history.adapter = adapter
        ic_back.setOnClickListener { finish() }
    }

    override fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
//            val lastCash = withContext(Dispatchers.IO) {
//                kotlin.runCatching {
//                    ApiHelper.api.getLastCash()
//                }.getOrNull()
//            }
            val lastCash = JSONObject.parseObject(dd)
            lastCashData = if (lastCash != null) {
                lastCash.getJSONObject("data")
            } else {
                null
            }
            bindUI()
        }
    }

    private fun bindUI() {
        if (lastCashData == null) {
            //加载失败页面
            return
        }
        adapter.refreshData(lastCashData!!.getJSONArray("speedInfo"))
        val speedConfig = lastCashData!!.getJSONObject("speedConfig")
        if (speedConfig != null) {
            speed_button.text = speedConfig.getString("infoSpeedBtnTxt")
            if (speedConfig.getBooleanValue("toDayIsSpeed")) {
                //当天可以加速
            } else {
                //当天不可以加速
            }
            cash_value.text = speedConfig.getString("txMoney")
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