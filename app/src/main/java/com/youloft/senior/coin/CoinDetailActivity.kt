package com.youloft.senior.coin

import androidx.recyclerview.widget.LinearLayoutManager
import com.youloft.core.base.BaseActivity
import com.youloft.net.ApiHelper
import com.youloft.senior.R
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
            if (result == null || !result.has("data")) {
                return@launch
            }
            val list = result.getAsJsonObject("data")
            if (list == null || !list.has("CoinLog")) {
                return@launch
            }
            adapter.refreshData(list.getAsJsonArray("CoinLog"))
        }
    }

    suspend fun request() = kotlinx.coroutines.withContext(Dispatchers.IO) {
        kotlin.runCatching {
            return@runCatching ApiHelper.api.getCoinDetail()
        }.getOrNull()
    }

}