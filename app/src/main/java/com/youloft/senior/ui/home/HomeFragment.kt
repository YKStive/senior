package com.youloft.senior.ui.home

import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.itembinder.PostInviteViewBinder
import com.youloft.senior.itembinder.PostLocalAlbumViewBinder
import com.youloft.senior.itembinder.PostRemoteViewBinder
import com.youloft.senior.itembinder.PostPunchViewBinder
import com.youloft.coolktx.dp2px
import com.youloft.senior.utils.logD
import com.youloft.senior.widgt.RecycleViewDivider
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 信息流界面
 */
class HomeFragment : BaseVMFragment() {

    private val mViewModel by viewModels<HomeViewModel>()
    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mAdapter.register(Post::class).to(
            PostRemoteViewBinder({ userId ->
                "个人界面".logD()
            }, { postId, openComment ->
                if (openComment) {
                    "评论".logD()
                } else {
                    "详情".logD()
                }
            }, { postId ->
                "分享".logD()
            }, { postId ->
                "点赞".logD()
            }),

            PostLocalAlbumViewBinder({
                "去发表页面".logD()
            }, {
                "立即分享".logD()
            }),

            PostPunchViewBinder { btnPunch ->
                "立即签到".logD()
                btnPunch.isSelected = true
            },

            PostInviteViewBinder { _: Button ->
                "邀请好友".logD()
            }
        ).withKotlinClassLinker { _, item ->
            when (item.postType) {
                PostType.IMAGE_TEXT -> PostRemoteViewBinder::class
                PostType.GIF -> PostRemoteViewBinder::class
                PostType.ALBUM -> PostRemoteViewBinder::class
                PostType.VIDEO -> PostRemoteViewBinder::class
                PostType.LOCAL_ALBUM -> PostLocalAlbumViewBinder::class
                PostType.INVITE -> PostInviteViewBinder::class
                PostType.PUNCH -> PostPunchViewBinder::class
                else -> PostRemoteViewBinder::class
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

    override fun initData() {
        mViewModel.getData()
    }

    override fun startObserve() {
        mViewModel.data.observe(this, Observer {
            mAdapter.items = it
            mAdapter.notifyDataSetChanged()
        })
    }


}