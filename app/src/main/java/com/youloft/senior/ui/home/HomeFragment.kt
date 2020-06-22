package com.youloft.senior.ui.home

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.itembinder.MultiImageViewBinder
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 信息流界面
 */
class HomeFragment : BaseVMFragment<HomeViewModel>() {

    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mAdapter.register(Post::class).to(
            MultiImageViewBinder({ userId ->
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
            })
        ).withKotlinClassLinker { position, item ->
            when (item.postType) {
                0 -> MultiImageViewBinder::class
                else -> MultiImageViewBinder::class
            }
        }

        rv_post.run {
            layoutManager = LinearLayoutManager(App.instance())
            adapter = mAdapter
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

    override fun initVM(): HomeViewModel {
        return HomeViewModel()
    }

}