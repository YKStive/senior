package com.youloft.senior.ui.gif

import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_gif_preview.*

/**
 * @author you
 * @create 2020/6/24
 * @desc gif预览
 */
class GifPreviewActivity : BaseActivity() {

    private lateinit var mResultGif: String
    private val mViewModel by viewModels<GifViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_gif_preview
    }


    override fun initView() {
        common_title.onBack {
            finish()
        }

        tv_publish.setOnClickListener {
            val post = Post(postType = PostType.GIF)
            post.mediaContent = arrayListOf<String>().apply {
                add(mResultGif)
            }
            PostPublishActivity.start(this, post)
        }

        tv_photo.setOnClickListener {
            ChoiceImageActivity.start(this, 1) {
                mViewModel.getData(it[0].path, mTempPath)
            }
        }

    }


    override fun initData() {
        if (mTempPath.isNotEmpty() && mResourcesPath.isNotEmpty()) {
            mViewModel.getData(mResourcesPath, mTempPath)
            mViewModel.resultData.observe(this, Observer {
                ImageLoader.loadImage(this, it, iv_gif)
                mResultGif = it
            })

        }
    }


    companion object {
        private var mTempPath = ""
        private var mResourcesPath = ""
        fun start(context: FragmentActivity, tempPath: String, resourcesPath: String) {
            mTempPath = tempPath
            mResourcesPath = resourcesPath
            val intent = Intent(context, GifPreviewActivity::class.java)
            context.startActivity(intent)
        }
    }

}