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
import com.youloft.senior.ui.adapter.CommentAdapterr
import kotlinx.android.synthetic.main.fragment_item_comment.*


/**
 *
 * @Description:     帖子评论列表
 * @Author:         slh
 * @CreateDate:     2020/6/22 18:05
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/22 18:05
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ItemCommentFragment : BaseVMFragment(), OnLoadMoreListener {
    companion object{
        fun newInstance(): ItemCommentFragment {
            val args = Bundle()
            val fragment = ItemCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mViewModel by viewModels<ItemCommnetViewModel>()
    lateinit var adapterr: CommentAdapterr
    override fun getLayoutResId(): Int = R.layout.fragment_item_comment


    override fun initView() {
        refreshLayout.setRefreshHeader(ClassicsHeader(activity))
        refreshLayout.setRefreshFooter(ClassicsFooter(activity))
        refreshLayout.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
        }
        refreshLayout.setOnLoadMoreListener(this)
        adapterr = CommentAdapterr(null)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapterr

    }

    override fun initData() {

    }

    override fun startObserve() {
        mViewModel._data.observe(this, Observer {
//            with(adapterr) { setNewInstance(it) }
        })
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore(2000 /*,false*/) //传入false表示加载失败
    }
}