package com.youloft.senior.ui.gif

import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.ui.graphic.PostViewModel
import com.youloft.senior.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_gif_list.common_title
import kotlinx.android.synthetic.main.activity_gif_publish.*

/**
 * @author you
 * @create 2020/6/24
 * @desc gif发布
 */
class PostPublishActivity : BaseActivity() {

    val mViewModel by viewModels<PostViewModel>()

    private lateinit var mPaths: List<String>
    private lateinit var mPost: Post
    override fun getLayoutResId(): Int {
        return R.layout.activity_gif_publish
    }


    override fun initView() {
        common_title.onBack {
            finish()
        }

        tv_publish.setOnClickListener {
            val content = et_content.text
            if (content == null) {
                toast("说点什么吧！")
            } else {
                //todo 发表影集 detail singleTask
                mPost.textContent = content.toString()
                mViewModel.publishPost(mPost, mPaths as ArrayList<String>)
            }
        }

        iv_gif.setOnClickListener {
            if (mPaths.isNotEmpty() && mPaths.size > 1) {
                finish()
            }
        }
    }

    override fun initData() {
        mPost = intent.getSerializableExtra(POST)!! as Post
        mPaths = mPost.mediaContent
        if (mPaths.isNotEmpty()) {
            ImageLoader.loadImage(this@PostPublishActivity, mPaths[0], iv_gif)
        }

        mViewModel.liveData.observe(this, Observer {
            if (it.showLoading) {showLoading()} else {dismissLoading()}
            it.showError?.let { it1 -> toast(it1) }
            if (it.isSuccess) {
                toast("帖子发表成功，审核通过后别人就可以看到啦")
                finish()
            }
        })
    }


    companion object {
        private const val POST = "post"
        fun start(context: FragmentActivity, post: Post) {
            val intent = Intent(context, PostPublishActivity::class.java)
            intent.putExtra(POST, post)
            context.startActivity(intent)
        }
    }

}