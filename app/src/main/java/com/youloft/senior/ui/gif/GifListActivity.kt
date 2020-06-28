package com.youloft.senior.ui.gif

import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseActivity
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.GifBean
import com.youloft.senior.itembinder.GifListViewBinder
import com.youloft.coolktx.dp2px
import com.youloft.senior.utils.logD
import com.youloft.senior.widgt.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_gif_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        rv_gif.addItemDecoration(
            RecycleViewDivider(
                App.instance(),
                LinearLayoutManager.HORIZONTAL,
                20.dp2px,
                Color.parseColor("#ffffff")
            )
        )
        mAdapter.register(GifBean::class, GifListViewBinder({ tempPath ->
            ChoiceImageActivity.start(this, 1) {
                val imageRes = it[0].path
                GifPreviewActivity.start(this, tempPath, imageRes)
            }
        }, {
            GifPublishActivity.start(this, it)
        }))
        rv_gif.adapter = mAdapter

    }

    override fun initData() {
        lifecycleScope.launchIOWhenCreated({
            it.message?.logD()
        }, {
            val stickers = ApiHelper.api.getStickers()
//            val stickers = NetResponse<List<GifBean>>(getMock(), "", "", 200)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(stickers, {
                    mAdapter.items = it
                    mAdapter.notifyDataSetChanged()
                })
            }
        })
    }

    fun getMock(): List<GifBean> {
        val mutableListOf = mutableListOf<GifBean>()
        repeat(20) {
            mutableListOf.add(
                GifBean(
                    it,
                    "https://media0.giphy.com/media/26BRyql7J3iOx875u/200.gif",
                    "https://media0.giphy.com/media/26BRyql7J3iOx875u/200.gif"
                )
            )
        }
        return mutableListOf
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, GifListActivity::class.java)
            context.startActivity(intent)
        }
    }
}