package com.youloft.senior.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.fragment_item_comment.*


/**
 *
 * @Description:     帖子点赞头像列表
 * @Author:         slh
 * @CreateDate:     2020/6/23 10:33
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 10:33
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class FavoriteFragment : BaseVMFragment() {
    companion object {
        fun newInstance(): FavoriteFragment {
            val args = Bundle()
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mViewModel by viewModels<ItemFavoriteViewModel>()
    lateinit var adapterr: FavoriteAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_item_comment


    override fun initView() {
//        refreshLayout.setRefreshHeader(ClassicsHeader(activity))
//        refreshLayout.setRefreshFooter(ClassicsFooter(activity))
//        refreshLayout.setOnRefreshListener { refreshlayout ->
//            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
//        }
//        refreshLayout.setOnLoadMoreListener(this)
        adapterr = FavoriteAdapter(null)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapterr

    }

    override fun initData() {
        mViewModel.getData(HashMap<String, String>())
    }

    override fun startObserve() {
        mViewModel.resultData.observe(this, Observer {
            with(adapterr) { setList(it) }
        })
    }

//    override fun onLoadMore(refreshLayout: RefreshLayout) {
//        refreshLayout.finishLoadMore(2000 /*,false*/) //传入false表示加载失败
//    }
}