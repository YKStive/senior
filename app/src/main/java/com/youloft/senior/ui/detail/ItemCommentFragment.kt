package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.google.gson.Gson
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.ConstConfig
import com.youloft.senior.R
import com.youloft.senior.bean.PraiseBean
import com.youloft.senior.bean.PraiseComment
import com.youloft.senior.bean.User
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.utils.DiffCommentCallback
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.UserManager
import com.youloft.senior.utils.logD
import com.youloft.util.ToastMaster
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
    var postUserId: String = ""
    var optionType = ConstConfig.REQUEST_REFRESH

    companion object {
        fun newInstance(postId: String): ItemCommentFragment {
            val args = Bundle()
            args.putString("postId", postId)
            val fragment = ItemCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var mViewModel: DetailViewModel? = null

    //    private val mViewModel by viewModels<ItemCommnetViewModel>()
    lateinit var adapter: CommentAdapterr
    override fun getLayoutResId(): Int = R.layout.fragment_item_comment


    override fun initView() {

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
                if (!UserManager.instance.hasLogin()) {
                    activity?.let { LoginDialog(it, lifecycleScope).show() }
                    return
                }
                when (view.id) {
                    R.id.ll_favorite -> {
                        lifecycleScope.launchIOWhenCreated({
                            ToastMaster.showShortToast(activity, it.message.toString())
                            it.message?.logD()
                        }, {
//            val stickers = ApiHelper.api.getStickers()
                            var itemBean = adapter.data[position]
//TODO
                            var userManager = UserManager.instance
                            val res = ApiHelper.api.parse(
                                PraiseComment(
                                    itemBean.id,
                                    itemBean.avatar,
                                    itemBean.userId,
                                    itemBean.nickname,
                                    itemBean.content,
                                    postId,
                                    userManager.getAvatar(),
                                    userManager.getUserId(),
                                    userManager.getNickname(),
                                    postUserId
                                )
                            )
                            withContext(Dispatchers.Main) {
                                if (res.status == 200) {
                                    if (itemBean.isPraised) {//是取消点赞
                                        itemBean.praised -= 1
                                        itemBean.isPraised = false
                                    } else {
                                        itemBean.praised += 1
                                        itemBean.isPraised = true
                                        //点赞
                                    }
                                    adapter.notifyItemChanged(position, CommentAdapterr.PAYLOAD)
                                }
                            }
                        })
                    }
                }
            }
        })
    }

    override fun initData() {
        val userStr by Preference(Preference.USER_INFO, "")
//        userInfo = gson.fromJson(userStr, User.javaClass)
        var recivePostId = arguments?.getString("postId");
        if (!recivePostId.isNullOrBlank()) {
            postId = recivePostId
        }
    }

    override fun startObserve() {
        mViewModel = activity?.let { ViewModelProvider(it).get(DetailViewModel::class.java) }
        var params = HashMap<String, String>()
        params.put("postId", postId)
        params.put("limit", ConstConfig.limit.toString())
        if (UserManager.instance.hasLogin()) {
            params.put("userId", UserManager.instance.getUserId())
        }
        mViewModel?.getCommentList(params)
        activity?.let { ctx ->
            mViewModel?.let { viewModle ->
                viewModle.commentResultData.observe(ctx, Observer {
                    if (optionType == ConstConfig.REQUEST_REFRESH) {
                        adapter.setList(it)
                    } else {
                        adapter.addData(it)
                        adapter.loadMoreModule.loadMoreComplete()
                    }
                    if (it.size < ConstConfig.limit) {
                        adapter.loadMoreModule.loadMoreEnd()
                    }
                })

                viewModle.postInfo.observe(ctx, Observer {
                    postUserId = it.userId
                })

                viewModle.addComment.observe(ctx, Observer {
                    if (it == 200) {//添加评论成功了
                        optionType = ConstConfig.REQUEST_REFRESH
                        var params = HashMap<String, String>()
                        params.put("postId", postId)
                        params.put("limit", "1")
                        if (UserManager.instance.hasLogin()) {
                            params.put("userId", UserManager.instance.getUserId())
                        }
                        viewModle.getCommentList(params)
                    }
                })
            }


        }

        initLoadMore()
    }

    //
    fun initLoadMore() {
        adapter.loadMoreModule.setOnLoadMoreListener {
            optionType = ConstConfig.REQUEST_MOREDATA
//            var recivePostId = arguments?.getString("postId");
//            if (!recivePostId.isNullOrBlank()) {
//                postId = recivePostId
//            }
            var params = HashMap<String, String>()
            params.put("postId", postId)
            params.put("limit", ConstConfig.limit.toString())
            params.put("index", adapter.data[adapter.data.lastIndex].id)
            if (UserManager.instance.hasLogin()) {
                params.put("userId", UserManager.instance.getUserId())
            }
            mViewModel?.getCommentList(params)
        }
//        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        adapterr.loadMoreModule.isEnableLoadMore(false)
    }
}