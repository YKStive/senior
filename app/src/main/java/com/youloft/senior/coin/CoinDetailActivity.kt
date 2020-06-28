package com.youloft.senior.coin

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.net.ApiHelper
import kotlinx.android.synthetic.main.activity_coin_detail_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author xll
 * @date 2020/6/23 15:58
 */
class CoinDetailActivity : BaseActivity() {
    val adapter by lazy { CoinDetailAdapter() }
    override fun getLayoutResId(): Int {
        return R.layout.activity_coin_detail_layout
    }

    override fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        ic_back.setOnClickListener { finish() }
    }

    override fun initData() {
        //加载数据
        GlobalScope.launch(Dispatchers.Main) {
            val result = request()
            if (result == null || !result.containsKey("data")) {
                return@launch
            }
            val list = result.getJSONObject("data")
            if (list == null || !list.containsKey("CoinLog")) {
                return@launch
            }
            adapter.refreshData(list.getJSONArray("CoinLog"))
            empty_view.visibility = if (adapter.itemCount > 0) View.GONE else View.VISIBLE
        }
    }

    suspend fun request() = kotlinx.coroutines.withContext(Dispatchers.IO) {
        kotlin.runCatching {
            ApiHelper.api.getCoinDetail()
        }.getOrNull()
    }

}