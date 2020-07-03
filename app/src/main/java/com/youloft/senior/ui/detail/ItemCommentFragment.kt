package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseVMFragment
import com.youloft.net.bean.CommentBean
import com.youloft.senior.ConstConfig
import com.youloft.senior.R
import com.youloft.senior.bean.DeleteCommentUploadParams
import com.youloft.senior.bean.PraiseComment
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.utils.DiffCommentCallback
import com.youloft.senior.utils.UserManager
import com.youloft.senior.utils.logD
import com.youloft.senior.widgt.CommentOptionPopup
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.fragment_item_comment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.lastIndex
import kotlin.collections.set


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

    //是否是用户当前增加评论
    private var addNewComment = false

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
                            val itemBean = adapter.data[position]
                            val userManager = UserManager.instance
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
                                    }
                                    adapter.notifyItemChanged(position, CommentAdapterr.PAYLOAD)
                                }
                            }
                        })
                    }
                }
            }
        })
        adapter.setOnItemLongClickListener { _, _, position ->
            if (!UserManager.instance.hasLogin()) {
                activity?.let { LoginDialog(it, lifecycleScope).show() }
                return@setOnItemLongClickListener true
            }
            val commnetBean = adapter.data[position]
            if (UserManager.instance.getUserId().equals(commnetBean.userId)) {
                val popup = CommentOptionPopup(activity, {
                    lifecycleScope.launchIOWhenCreated({
                        it.message?.logD()
                    }, {
                        val result = ApiHelper.api.deleteComment(
                            DeleteCommentUploadParams(
                                commnetBean.id,
                                postId
                            )
                        )
                        withContext(Dispatchers.Main) {
                            ApiHelper.executeResponse(result, {
                                if (result.isSuccess()) {
                                    ToastMaster.showShortToast(activity, "删除成功")
                                    mViewModel?.addOrDeleteComment?.value = ""
                                    adapter.removeAt(position)
                                } else {
                                    ToastMaster.showShortToast(activity, result.msg)
                                }
                            })
                        }
                    })
                }, adapter.data[position].content, "删除")
                popup.showAtLocation(recyclerView, Gravity.CENTER, 0, 0)
            } else {
                val popup = CommentOptionPopup(activity, {
//TODO 埋点
                }, adapter.data[position].content, "举报")
                popup.showAtLocation(recyclerView, Gravity.CENTER, 0, 0)
            }

            true
        }
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
        params["postId"] = postId
        params["limit"] = ConstConfig.limit.toString()
        if (UserManager.instance.hasLogin()) {
            params["userId"] = UserManager.instance.getUserId()
        }
        addNewComment = false
        mViewModel?.getCommentList(params)
        activity?.let { ctx ->
            mViewModel?.let { viewModle ->
                //评论列表
                viewModle.commentResultData.observe(ctx, Observer {
                    if (addNewComment) {
                        // 先拷贝出数据
                        val data: MutableList<CommentBean> = ArrayList(adapter.data)
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
                //监听帖子信息
                viewModle.postInfo.observe(ctx, Observer {
                    postUserId = it.userId
                })
                //当前用户评论了这条帖子 todo 只增加一条，不能直接setList
                viewModle.addOrDeleteComment.observe(ctx, Observer {
                    if (it.isNotEmpty()) {//添加评论成功了
                        //拉去用户最新一条评论，即刚才发表的那条评论
                        addNewComment = true
                        val addCommnetParams = HashMap<String, String>()
                        addCommnetParams["postId"] = postId
                        addCommnetParams["limit"] = "1"
                        if (UserManager.instance.hasLogin()) {
                            addCommnetParams["userId"] = UserManager.instance.getUserId()
                        }
                        viewModle.getCommentList(addCommnetParams)
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
            val params = HashMap<String, String>()
            params["postId"] = postId
            params["limit"] = ConstConfig.limit.toString()
            params["index"] = adapter.data[adapter.data.lastIndex].id
            if (UserManager.instance.hasLogin()) {
                params["userId"] = UserManager.instance.getUserId()
            }
            addNewComment = false
            mViewModel?.getCommentList(params)
        }
//        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
//        adapterr.loadMoreModule.isEnableLoadMore(false)
    }
}