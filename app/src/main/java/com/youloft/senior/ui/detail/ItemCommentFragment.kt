package com.youloft.senior.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.youloft.core.base.BaseVMFragment
import com.youloft.net.bean.CommentData
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.utils.TAG
import com.youloft.senior.utils.logE
import com.youloft.senior.widgt.CustomLinearLayoutManager
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
    companion object {
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
        var data =mutableListOf<CommentData>()
        for(i in 0..10){
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
        }

        adapterr = CommentAdapterr(data)
        var manager=CustomLinearLayoutManager(activity)
        recyclerView.layoutManager =manager
        recyclerView.adapter = adapterr
//        var scrollY = recyclerView.computeVerticalScrollOffset();
//        TAG.logE("滑动=${scrollY}")
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var mmRvScrollY = 0 // 列表滑动距离
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                mmRvScrollY += dy
                TAG.logE( "onScrolled: mmRvScrollY: $mmRvScrollY, dy: $dy")
//                manager.canScroll=mmRvScrollY>100
            }
        })
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