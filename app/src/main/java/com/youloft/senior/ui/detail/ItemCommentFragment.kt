package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.util.ToastMaster
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
class ItemCommentFragment : BaseVMFragment() {
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
//        refreshLayout.setRefreshHeader(ClassicsHeader(activity))
//        refreshLayout.setRefreshFooter(ClassicsFooter(activity))
//        refreshLayout.setOnRefreshListener { refreshlayout ->
//            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
//        }
//        refreshLayout.setOnLoadMoreListener(this)

        adapterr = CommentAdapterr(null)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapterr
        adapterr.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
                when(view.id){
                    R.id.ll_favorite->{
                        ToastMaster.showShortToast(activity, "点赞")
                    }
                }
            }
        })
        initLoadMore()
//
    }

    override fun initData() {
        mViewModel.getData(HashMap<String, String>())
    }

    override fun startObserve() {
        mViewModel.resultData.observe(this, Observer {
//            with(adapterr) { setList(it) }
            adapterr.setList(it)
        })
    }

//    override fun onLoadMore(refreshLayout: RefreshLayout) {
//        refreshLayout.finishLoadMore(2000 /*,false*/) //传入false表示加载失败
//    }

    fun initLoadMore(){
        adapterr.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
            //TODO
        })
        adapterr.loadMoreModule.isAutoLoadMore=true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        adapterr.loadMoreModule.isEnableLoadMore(false)
    }
}