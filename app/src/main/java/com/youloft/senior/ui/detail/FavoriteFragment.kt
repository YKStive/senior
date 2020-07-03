package com.youloft.senior.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.ConstConfig
import com.youloft.senior.R
import com.youloft.senior.bean.FavoriteHeadBean
import com.youloft.senior.ui.adapter.FavoriteAdapter
import com.youloft.senior.utils.DiffFavoriteCallback
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.fragment_item_comment.*
import java.util.ArrayList


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
    private var optionType = ConstConfig.REQUEST_REFRESH
    var postId = ""
    var mViewModel: DetailViewModel? = null

    //当前是增加一条还是减少一条
    private var addFavorite = false

    companion object {
        fun newInstance(postId: String): FavoriteFragment {
            val args = Bundle()
            args.putString("postId", postId)
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }


    lateinit var adapter: FavoriteAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_item_comment


    override fun initView() {
        adapter = FavoriteAdapter(null)
        adapter.setDiffCallback(DiffFavoriteCallback())
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        initLoadMore()
    }

    override fun initData() {
        val recivePostId = arguments?.getString("postId")
        if (!recivePostId.isNullOrBlank()) {
            postId = recivePostId
        }

    }

    override fun startObserve() {
        mViewModel = activity?.let { ViewModelProvider(it).get(DetailViewModel::class.java) }
        val params = HashMap<String, String>()
        params.put("postId", postId)
        params.put("limit", ConstConfig.limit.toString())
//        if (UserManager.instance.hasLogin()) {
//            params.put("userId", UserManager.instance.getUserId())
//        }
        mViewModel?.favorite(params) {
            ToastMaster.showShortToast(activity, it)
        }
        activity?.let { ctx ->
            mViewModel?.let {
                //获取点赞头像列表
                it.favoriteResultData.observe(ctx, Observer {
                    if (addFavorite) {//增加当前用户点赞
                        // 先拷贝出数据
                        val data: MutableList<FavoriteHeadBean> = ArrayList(adapter.data)
                        data.addAll(it)
                        // 使用 diif 刷新数据
                        adapter.setDiffNewData(data)
                    } else {
                        if (optionType == ConstConfig.REQUEST_REFRESH) {
                            adapter.setList(it)
                        } else {
                            adapter.addData(it)
                            adapter.loadMoreModule.loadMoreComplete()
                        }
                        if (it.size < ConstConfig.limit) {
                            adapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                })
                //当前用户点赞这条帖子，需要增加一条
                it.addOrCancleFavorite.observe(ctx, Observer {
                    if (it.isEmpty()) {//取消点赞
                        addFavorite = false
                        if (adapter.data.size > 0) {
                            adapter.removeAt(0)
                        }
                    } else {
                        //拉取最新一条点赞
                        addFavorite = true
                        //点赞
                        val params = HashMap<String, String>()
                        params.put("postId", postId)
                        params.put("limit", "1")
                        mViewModel?.favorite(params) {
                            ToastMaster.showShortToast(activity, it)
                        }
                    }
                })
            }
        }
    }

    fun initLoadMore() {
        adapter.loadMoreModule.setOnLoadMoreListener {
            optionType = ConstConfig.REQUEST_MOREDATA
            val recivePostId = this.arguments?.getString("postId");
            if (!recivePostId.isNullOrBlank()) {
                postId = recivePostId
            }
            val params = HashMap<String, String>()
            params["postId"] = postId
            params["limit"] = ConstConfig.limit.toString()
            params["index"] = adapter.data[adapter.data.lastIndex].id
//            if (UserManager.instance.hasLogin()) {
//                params.put("userId", UserManager.instance.getUserId())
//            }
            mViewModel?.favorite(params) {
                ToastMaster.showShortToast(activity, it)
            }
        }
//        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        adapterr.loadMoreModule.isEnableLoadMore(false)
    }
}