package com.youloft.senior.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.ConstConfig
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.FavoriteAdapter
import com.youloft.senior.utils.UserManager
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
    var optionType = ConstConfig.REQUEST_REFRESH
    var postId: String = ""

    companion object {
        fun newInstance(postId: String): FavoriteFragment {
            val args = Bundle()
            args.putString("postId", postId)
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mViewModel by viewModels<ItemFavoriteViewModel>()
    lateinit var adapter: FavoriteAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_item_comment


    override fun initView() {
        adapter = FavoriteAdapter(null)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        initLoadMore()
    }

    override fun initData() {
        var recivePostId = arguments?.getString("postId");
        if (!recivePostId.isNullOrBlank()) {
            postId = recivePostId
        }
        var params = HashMap<String, String>()
        params.put("postId", postId)
        params.put("limit", ConstConfig.limit.toString())
//        if (UserManager.instance.hasLogin()) {
//            params.put("userId", UserManager.instance.getUserId())
//        }
        mViewModel.getData(params)
    }

    override fun startObserve() {
        mViewModel.resultData.observe(this, Observer {
            if (it.size < ConstConfig.limit) {
                adapter.loadMoreModule.loadMoreEnd()
            }
            if (optionType == ConstConfig.REQUEST_REFRESH) {
                adapter.setList(it)
            } else {
                adapter.addData(it)
            }
        })
    }

    fun initLoadMore() {
        adapter.loadMoreModule.setOnLoadMoreListener {
            optionType = ConstConfig.REQUEST_MOREDATA
            var recivePostId = arguments?.getString("postId");
            if (!recivePostId.isNullOrBlank()) {
                postId = recivePostId
            }
            var params = HashMap<String, String>()
            params.put("postId", postId)
            params.put("limit", ConstConfig.limit.toString())
            params.put("index", adapter.data[adapter.data.lastIndex].id)
//            if (UserManager.instance.hasLogin()) {
//                params.put("userId", UserManager.instance.getUserId())
//            }
            mViewModel.getData(params)
        }
//        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        adapterr.loadMoreModule.isEnableLoadMore(false)
    }
}