package com.youloft.senior.cash

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import kotlinx.android.synthetic.main.activity_cash_layout.*

/**
 * @author xll
 * @date 2020/6/24 10:20
 */
class CashActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_cash_layout
    }

    var selectCashItem: JSONObject? = null

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
        selectCashItem = cash_list_view.getSelectItem()
    }

    private fun refreshUI() {

    }

    override fun initData() {


    }
}