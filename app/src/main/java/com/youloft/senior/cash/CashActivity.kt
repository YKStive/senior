package com.youloft.senior.cash

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.widgt.ProgressHUD
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.activity_cash_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author xll
 * @date 2020/6/24 10:20
 */
class CashActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_cash_layout
    }

    var selectCashItem: JSONObject? = null
    var userWXMessage: JSONObject? = null;
    var lastCash: JSONObject? = null
    var cashListResult: JSONArray? = null

    override fun initView() {
        cash_list_view.setSelectCallBack {
            selectCashItem = it
            refreshUI()
        }
        ic_back.setOnClickListener { finish() }
        selectCashItem = cash_list_view.getSelectItem()
        cash_submit.setOnClickListener {
//            PhoneDialog(this).show()
            withDraw()
        }
        last_cash.setOnClickListener {
            startActivity(
                Intent(this, MoneyApplyProgressActivity::class.java)
                    .putExtra("caid", "111")
            )
        }
    }

    private fun refreshUI() {
        if (selectCashItem == null || userWXMessage == null) {
            loadError()
            return
        }
        val itemCashValue = selectCashItem!!.getFloatValue("price")
        if (itemCashValue > userWXMessage!!.getFloatValue("cash")) {
            short_group.visibility = View.VISIBLE
            short_text.text =
                "还差${(itemCashValue - userWXMessage!!.getFloatValue("cash"))}元即可提现，快去做任务赚钱吧"
        }
        coin_number.text = selectCashItem!!.getIntValue("coin").toString()
    }

    override fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            val userInfo = requestUserCoinInfo()
            val cashList = requestCoinList()
            val cashRecord = requestUserRecord()
//            if (userInfo == null) {
//                loadError()
//                return@launch
//            }
//            if (userInfo.getIntValue("status") != 200 || userInfo.getJSONObject("data") == null) {
//                loadError()
//                return@launch
//            }
//            userInfo = userInfo.getJSONObject("data")
            if (cashList == null) {
                loadError()
                return@launch
            }
            if (cashList.getIntValue("status") != 200 || !cashList.containsKey("data")) {
                loadError()
                return@launch
            }
            val cashListResult = cashList.getJSONArray("data")
            userWXMessage = userInfo
            lastCash = cashRecord
            this@CashActivity.cashListResult = cashListResult
            bindUI()
        }
    }

    private fun refreshUser() {
        GlobalScope.launch(Dispatchers.Main) {
            val userInfo = requestUserCoinInfo()
            userWXMessage = userInfo
            bindUI()
        }
    }

    private fun showProcess(s: String) {
        if (isFinishing) return
        if (progressHUD != null && progressHUD!!.isShowing()) {
            progressHUD!!.dismiss()
        }
        progressHUD = ProgressHUD.show(this, s)
    }


    var progressHUD: ProgressHUD? = null

    private fun closeProgressHUD() {
        if (isFinishing) return
        if (progressHUD != null && progressHUD!!.isShowing()) {
            progressHUD!!.dismiss()
        }
    }

    fun withDraw() {
        if (selectCashItem == null) {
            return
        }
        showProcess("申请提交中")
        GlobalScope.launch(Dispatchers.Main) {
            val selectType = selectCashItem!!.getIntValue("type")
            val result = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    ApiHelper.api.withDraw(
                        selectCashItem!!.getIntValue("cash"),
                        0, selectCashItem!!.getIntValue("type")
                    )
                }.getOrNull()
            }
            closeProgressHUD()
            if (result == null) {
                ToastMaster.showLongToast(this@CashActivity, "网络异常")
                return@launch
            }
            if (result.getBooleanValue("success")) {
                //提现成功
                if (selectType == 0) {
                    //新人专享
                    val speedMode = result.getJSONObject("speedModel")
                    if (speedMode != null) {
                        val speedConfig = speedMode.getJSONObject("speedConfig")
                        if (speedConfig != null) {
                            showCashTips(speedConfig.getString("txMoney"))
                        }
                    }
                } else {
                    //普通提现
                    val speedMode = result.getJSONObject("speedModel")
                    if (speedMode != null) {
                        val speedConfig = speedMode.getJSONObject("speedConfig")
                        if (speedConfig != null) {
                            showNormal(speedConfig.getString("txMoney"), speedMode)
                        }
                    }
                }
                return@launch
            }
            if (!TextUtils.isEmpty(result.getString("msg"))) {
                ToastMaster.showLongToast(this@CashActivity, result.getString("msg"))
                return@launch
            }
            ToastMaster.showLongToast(this@CashActivity, "网络异常")
        }
    }

    private fun showCashTips(money: String) {
        CashTipsDialog(this) {
            //看视频直接触发提现
        }.bindMoney(money).show()
    }

    private fun showNormal(money: String, speedModel: JSONObject?) {
        NormalTipsDialog(this) {
            startActivity(
                Intent(this, MoneyApplyProgressActivity::class.java)
                    .putExtra("caid", speedModel?.getString("caid"))
            )
        }.bindMoney(money).show()
    }

    private fun bindUI() {
        if (userWXMessage == null) {
            loadError()
            return
        }
        if (cashListResult != null) {
            cash_list_view.refresh(cashListResult)
        }
        my_coin.text = userWXMessage!!.getIntValue("coin").toString()
        top_cash.text = userWXMessage!!.getString("cash")
        selectCashItem = cash_list_view.getSelectItem()
        refreshUI()
    }

    /**
     * 加载失败
     */
    private fun loadError() {

    }

    suspend fun requestUserCoinInfo() = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            ApiHelper.api.getUserCoinInfo()
        }.getOrNull()
    }

    suspend fun requestCoinList() = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            ApiHelper.api.getCashList()
        }.getOrNull()
    }

    suspend fun requestUserRecord() = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            ApiHelper.api.getLastCash()
        }.getOrNull()
    }
}