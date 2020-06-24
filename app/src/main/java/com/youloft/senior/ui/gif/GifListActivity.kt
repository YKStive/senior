package com.youloft.senior.ui.gif

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.core.base.BaseActivity
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.R
import com.youloft.senior.bean.GifBean
import com.youloft.senior.itembinder.GifListViewBinder
import com.youloft.senior.net.Api
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.activity_gif_list.*

/**
 * @author you
 * @create 2020/6/24
 * @desc gif列表
 */
class GifListActivity : BaseActivity() {

    private val mAdapter = MultiTypeAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.activity_gif_list
    }


    override fun initView() {
        common_title.onBack {
            finish()
        }
        common_title.setTitle(getString(R.string.make_gif))

        rv_gif.layoutManager = GridLayoutManager(applicationContext, 2)
        mAdapter.register(GifBean::class, GifListViewBinder({
            it.logD()
        }, {
            it.logD()
        }))

    }

    override fun initData() {
        lifecycleScope.launchWhenCreated {
            val stickers = ApiHelper.api.getStickers()
            ApiHelper.executeResponse(stickers, {

            }, {

            })
        }
    }
}