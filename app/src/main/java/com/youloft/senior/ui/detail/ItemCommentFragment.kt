package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.google.gson.Gson
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.bean.User
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.utils.DiffCommentCallback
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.fragment_item_comment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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
    var postId: String = ""
    lateinit var userInfo: User
    var gson = Gson();

    companion object {
        fun newInstance(postId: String): ItemCommentFragment {
            val args = Bundle()
            args.putString("postId", postId)
            val fragment = ItemCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mViewModel by viewModels<ItemCommnetViewModel>()
    lateinit var adapter: CommentAdapterr
    override fun getLayoutResId(): Int = R.layout.fragment_item_comment


    override fun initView() {
//        refreshLayout.setRefreshHeader(ClassicsHeader(activity))
//        refreshLayout.setRefreshFooter(ClassicsFooter(activity))
//        refreshLayout.setOnRefreshListener { refreshlayout ->
//            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
//        }
//        refreshLayout.setOnLoadMoreListener(this)

        adapter = CommentAdapterr(null)
        adapter.setDiffCallback(DiffCommentCallback())
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                baseAdapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
                when (view.id) {
                    R.id.ll_favorite -> {
                        lifecycleScope.launchIOWhenCreated({
                            it.message?.logD()
                        }, {
//            val stickers = ApiHelper.api.getStickers()
                            var params = HashMap<String, String>()
                            var itemBean = adapter.data[position]
//TODO
                            params.put("postId", postId)
                            params.put("userId", itemBean.id)
                            params.put("avatar", itemBean.id)
                            params.put("nickname", itemBean.id)
//                            val res = NetResponse<String>(ApiHelper.api.parse(params).data, "", "", 200)
                            val res = ApiHelper.api.parse(params)
                            withContext(Dispatchers.Main) {
                                if (res.status == 200) {

                                }
//                                ApiHelper.executeResponse(res, {
//                                    if (res.status==200)
//                                })
                            }
                        })
                    }
                }
            }
        })
        initLoadMore()
//
    }

    override fun initData() {
        val userStr by Preference(Preference.USER_INFO, "")
//        userInfo = gson.fromJson(userStr, User.javaClass)
        var recivePostId = arguments?.getString("postId");
        if (!recivePostId.isNullOrBlank()) {
            postId = recivePostId
        }
        mViewModel.getData(HashMap<String, String>())
    }

    override fun startObserve() {
        mViewModel.resultData.observe(this, Observer {
//            with(adapterr) { setList(it) }
            adapter.setList(it)
        })
    }

//    override fun onLoadMore(refreshLayout: RefreshLayout) {
//        refreshLayout.finishLoadMore(2000 /*,false*/) //传入false表示加载失败
//    }

    fun initLoadMore() {
        adapter.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
            //TODO
        })
        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        adapterr.loadMoreModule.isEnableLoadMore(false)
    }
}