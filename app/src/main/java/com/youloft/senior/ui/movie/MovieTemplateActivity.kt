package com.youloft.senior.ui.movie

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.coolktx.toJsonString
import com.youloft.core.base.BaseActivity
import com.youloft.senior.ConstConfig
import com.youloft.senior.R
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.adapter.MovieTemplateAdapter
import com.youloft.senior.ui.detail.WebViewActivity
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.utils.logD
import com.youloft.senior.widgt.GridSpaceItemDecoration
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.activity_movie_template.*
import kotlinx.android.synthetic.main.conmon_title.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 *
 * @Description:    影集模板
 * @Author:         slh
 * @CreateDate:     2020/7/3 10:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/3 10:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MovieTemplateActivity : BaseActivity() {
    private var pageNo = 1
    private lateinit var mAdapter: MovieTemplateAdapter
    override fun getLayoutResId(): Int {
        return R.layout.activity_movie_template
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MovieTemplateActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView() {
        image_back.setOnClickListener {
            finish()
        }
        tv_title.setText(R.string.select_movie_template)
        mAdapter = MovieTemplateAdapter()
        recycler_view.run {
            layoutManager = GridLayoutManager(this@MovieTemplateActivity, 2)
            adapter = mAdapter
            addItemDecoration(GridSpaceItemDecoration(2, 9.dp2px, 15.dp2px))
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            val itemBean = mAdapter.data[position]
            ChoiceImageActivity.start(this, 20) {
                val imageRes = it.toJsonString()
                WebViewActivity.start(
                    this,
                    itemBean.material,
                    imageRes,
                    WebViewActivity.MOVIE_WEB_PREVIEW,
                    itemBean.code
                )
            }
        }
    }

    override fun initData() {
        lifecycleScope.launchIOWhenCreated({
            ToastMaster.showShortToast(this, it.message.toString())
            it.message?.logD()
        }, {
            val res = ApiHelper.api.getMovieTemplateList(pageNo, ConstConfig.limit)
            withContext(Dispatchers.Main) {
                if (res.status == 200) {
                    mAdapter.setList(res.data)
                }
            }
        })
    }
}