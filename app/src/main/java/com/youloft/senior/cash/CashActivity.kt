package com.youloft.senior.cash

import android.view.View
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.net.ApiHelper
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

    override fun initView() {
        val array = JSONArray()
        for (i in 0 until 7) {
            val item = JSONObject()
            item.put("code", "1")
            item.put("price", "0.3")
            item.put("txt", "0.3元")
            item.put("type", i)
            array.add(item)
        }
        cash_list_view.refresh(array)

        cash_list_view.setSelectCallBack {
            selectCashItem = it
            refreshUI()
        }
        ic_back.setOnClickListener { finish() }
        selectCashItem = cash_list_view.getSelectItem()
        cash_submit.setOnClickListener {
            PhoneDialog(this).show()
            withDraw()
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
        coin_number.text = selectCashItem!!.getIntValue("price").toString()
    }

    override fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            var userInfo = requestUserCoinInfo()
            val cashList = requestCoinList()
            val cashRecord = requestUserRecord()
            if (userInfo == null) {
                loadError()
                return@launch
            }
            if (userInfo.getIntValue("status") != 200 || userInfo.getJSONObject("data") == null) {
                loadError()
                return@launch
            }
            userInfo = userInfo.getJSONObject("data")
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
            bindUI(cashListResult)
        }
    }

    fun withDraw() {
        if (selectCashItem == null) {
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    ApiHelper.api.withDraw(
                        selectCashItem!!.getString("price"),
                        0, selectCashItem!!.getIntValue("type")
                    )
                }.getOrNull()
            }
        }
    }

    private fun bindUI(cashListResult: JSONArray) {
        if (userWXMessage == null) {
            loadError()
            return
        }
        cash_list_view.refresh(cashListResult)
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
            ApiHelper.api.getCashRecord()
        }.getOrNull()
    }
}