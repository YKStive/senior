package com.youloft.senior.ui.home

import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cc.shinichi.library.ImagePreview
import cc.shinichi.library.bean.ImageInfo
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.toast
import com.youloft.senior.base.URL_INVITE_FRIEND
import com.youloft.senior.coin.TaskManager
import com.youloft.senior.itembinder.*
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_COMMENT
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_CONTENT
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_HEADER
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_ITEM
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_PRAISE
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_SHARE
import com.youloft.senior.ui.detail.DetailActivity
import com.youloft.senior.ui.graphic.InviteFriendActivity
import com.youloft.senior.ui.graphic.PostViewModel
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import com.youloft.senior.widgt.RecycleViewDivider
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.share.ShareImage
import com.youloft.socialize.share.ShareWeb
import com.youloft.socialize.share.UmengShareActionImpl
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 信息流界面
 */
class HomeFragment : BaseVMFragment() {

    private val mViewModel by viewModels<HomeViewModel>()
    private val mPostViewModel by viewModels<PostViewModel>()
    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        refreshLayout.setRefreshHeader(ClassicsHeader(App.instance()))
        refreshLayout.setRefreshFooter(ClassicsFooter(App.instance()))
        refreshLayout.setOnRefreshListener {
            mViewModel.getData(direction = 0)
        }

        refreshLayout.setOnLoadMoreListener {
            mViewModel.getData(direction = 1)
        }




        rv_post.setItemViewCacheSize(10)
        mAdapter.register(Post::class).to(

            //gif
            PostGifViewBinder { type, post ->
                deal(type, post)
            },

            //文字
            PostJustTextViewBinder { type, post -> },


            //多图
            PostMultiImageViewBinder { type, post ->

            },

            //视频
            PostVideoViewBinder { type, post ->

            },


            //本地影集
            PostLocalAlbumViewBinder({
                context?.toast("去影集")
            }, { localPhotoPaths ->
                this.activity?.let { hostActivity ->
                    LoginDialog.tryLogin(hostActivity) {
                        val post = Post(postType = PostType.ALBUM)
                        mPostViewModel.publishPost(post, localPhotoPaths)
                        mPostViewModel.liveData.observe(this, Observer {
                            if (it.showLoading) {
                                (hostActivity as HomeActivity).showLoading()
                            } else {
                                (hostActivity as HomeActivity).dismissLoading()
                            }
                            if (it.isSuccess) {
                                hostActivity.toast("去影集")
                            }
                            it.showError?.let { errorMsg ->
                                hostActivity.toast(errorMsg)
                            }
                        })
                    }
                }


            }),

            PostPunchViewBinder { btnPunch ->
                TaskManager.instance.sign(App.instance()) {
                    context?.toast("签到成功")
                    btnPunch.isSelected = true
                    //todo 签到后界面问题
                }
            },

            PostInviteViewBinder { _: Button ->
                this.activity?.let { InviteFriendActivity.start(it) }

            }
        ).withKotlinClassLinker { _, item ->
            val mediaContent = item.mediaContent
            when {
                item.postType == PostType.IMAGE_TEXT && mediaContent.isEmpty() -> PostJustTextViewBinder::class
                item.postType == PostType.IMAGE_TEXT && mediaContent.isNotEmpty() && mediaContent.size > 1 -> PostMultiImageViewBinder::class
                item.postType == PostType.IMAGE_TEXT && item.mediaContent.isNotEmpty() && mediaContent.size == 1 -> PostGifViewBinder::class
                item.postType == PostType.GIF -> PostGifViewBinder::class
                item.postType == PostType.LOCAL_ALBUM -> PostLocalAlbumViewBinder::class
                item.postType == PostType.VIDEO -> PostVideoViewBinder::class
                item.postType == PostType.INVITE -> PostInviteViewBinder::class
                item.postType == PostType.PUNCH -> PostPunchViewBinder::class
                else -> PostGifViewBinder::class
            }
        }

        rv_post.run {
            layoutManager = LinearLayoutManager(App.instance())
            adapter = mAdapter
            addItemDecoration(
                RecycleViewDivider(
                    App.instance(),
                    LinearLayoutManager.HORIZONTAL,
                    15.dp2px,
                    resources.getColor(R.color.app_home_bg)
                )
            )
        }
    }

    /**
     * 处理item点击
     */
    private fun deal(type: Int, post: Post) {
        when (type) {
            TYPE_ITEM, TYPE_COMMENT -> {
                goDetail(post)
            }

            TYPE_CONTENT -> {
                val result = mutableListOf<ImageInfo>()
                post.mediaContent.forEach { path ->
                    result.add(ImageInfo().apply {
                        originUrl = path
                        thumbnailUrl = path
                    })
                }
                ImagePreview
                    .getInstance()
                    .setContext(context as FragmentActivity)
                    .setIndex(0)
                    .setImageInfoList(result)
                    .setShowDownButton(false)
                    .setZoomTransitionDuration(500)
                    .start()
            }

            TYPE_HEADER -> {
                //TODO 个人界面

                activity?.toast("个人界面--${post.userId}")
            }


            TYPE_SHARE -> {
                share(post)
                activity?.toast("分享--${post.id}")
            }


            TYPE_PRAISE -> {
                //todo 点赞
                activity?.toast("点赞--${post.position}")

            }
        }
    }

    /**
     * 处理分享
     */
    private fun share(post: Post) {
        when (post.postType) {
            PostType.GIF -> {
                if (post.mediaContent.isNotEmpty()) {
                    UmengShareActionImpl(activity).platform(SOC_MEDIA.WEIXIN).image(
                        ShareImage(App.instance(), post.mediaContent[0])
                    ).perform()
                }
            }

            else -> {
                shareUrl(post)
            }

        }
    }

    /**
     * url分享
     */
    private fun shareUrl(post: Post) {
        //todo 不同type分享链接
        val url = when (post.postType) {
            else -> URL_INVITE_FRIEND

        }
        UmengShareActionImpl(activity).platform(SOC_MEDIA.WEIXIN).web(
            ShareWeb(url)
        ).perform()
    }

    /**
     * 帖子详情页面
     */
    private fun goDetail(post: Post) {
        activity?.let {
            DetailActivity.start(it, post.id, post.postType)
        }

    }

    /**
     * 去个人页面
     */
    private fun goPersonCenter(postId: String) {

    }

    override fun initData() {
        val autoRefresh = refreshLayout.autoRefresh()
        "刷新成功--${autoRefresh}".logE()
    }

    override fun startObserve() {
        mViewModel.listData.observe(this, Observer { liveData ->
            activity?.let { hostActivity ->
                val homeActivity = hostActivity as HomeActivity
                if (!liveData.showLoading) {
                    if (refreshLayout.isRefreshing) {
                        refreshLayout.finishRefresh()
                    } else if (refreshLayout.isLoading) {
                        refreshLayout.finishLoadMore()
                    }
                }
                liveData.showError?.let { homeActivity.toast(it) }

                liveData.showSuccess?.let {
                    mAdapter.items = it
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    /**
     * 点赞
     */
    private fun praise() {

        mViewModel.normalData.observe(this, Observer { normalLiveData ->
            activity?.let { hostActivity ->
                val homeActivity = hostActivity as HomeActivity
                if (normalLiveData.showLoading) {
                    homeActivity.showLoading()
                } else {
                    homeActivity.dismissLoading()
                }
                normalLiveData.showError?.let { homeActivity.toast(it) }
                normalLiveData.showSuccess?.let {

                }
            }
        })
    }


}