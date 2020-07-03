package com.youloft.senior.ui.graphic

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.coolktx.launchIOWhenStarted
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.dialog.ConfirmCancelDialog
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.ui.gif.ChoiceImageActivity.Companion.TYPE_ALL
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.activity_content_publish.*
import kotlinx.android.synthetic.main.layout_invite_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author you
 * @create 2020/6/29
 * @desc
 */
class ContentPublishActivity : BaseVMActivity() {
    private var justJump: Boolean = true
    private var mCurrentMode: Int = 0
    private val imageCountLimit = 20
    private val MODE_ADD = 0
    private val MODE_VIDEO = 1
    private val MODE_IMAGE = 2
    private val mViewModel by viewModels<PostViewModel>()
    private val mImageView by lazy {
        ContextImageView(this, null).apply {
            imageData.observe(this@ContentPublishActivity, Observer {
                when (it.size) {
                    1 -> {
                        showContent(MODE_ADD)
                    }
                    else -> {
                        make_album_container.visibility =
                            if (it.size >= 4) View.VISIBLE else View.GONE
                    }
                }
            })
        }
    }

    private val mVideoView by lazy {
        ContextVideoView(this, null).apply {
            emptyData.observe(this@ContentPublishActivity, Observer {
                showContent(MODE_ADD)
            })
        }
    }

    private val mAddView by lazy {
        ImageView(applicationContext).apply {
            layoutParams = FrameLayout.LayoutParams(112.dp2px, 112.dp2px)
            setImageResource(R.drawable.ic_placeholder_error)
            setOnClickListener {
                ChoiceImageActivity.start(this@ContentPublishActivity, imageCountLimit, TYPE_ALL) {
                    justJump = false
                    when (it[0].type) {
                        ImageRes.TYPE_VIDEO -> {
                            showContent(MODE_VIDEO)
                            mVideoView.setVideo(it[0])
                        }
                        ImageRes.TYPE_IMAGE -> {
                            showContent(MODE_IMAGE)
                            mImageView.setImages(it)
                        }
                    }
                }
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_content_publish
    }

    override fun initView() {
        showContent(MODE_ADD)
        common_title.onBack { onBackPressed() }

        tv_publish.setOnClickListener {
            val text = et_content.text
            val imageData = getImageData()
            if (text.isNullOrEmpty() && imageData == null) {
                toast("不能都为空")
                return@setOnClickListener
            }

            publish(text.toString(), imageData)
        }
        mAddView.performClick()
    }

    override fun onRestart() {
        super.onRestart()
        if (justJump) {
            finish()
        }
    }

    override fun onBackPressed() {
        ConfirmCancelDialog(this, getString(R.string.post_cancel_confirm), {
            finish()
            it.dismiss()
        }).apply {
            show()
            setConfirmText(getString(R.string.confirm_abandon))
            setCancelText(getString(R.string.cancel))
        }
    }

    /**
     * 发布帖子
     * @param content String 内容
     * @param imageData List<ImageRes>? 文件
     */
    private fun publish(content: String, imageData: List<ImageRes>?) {
        val filePath = ArrayList<String>()
        imageData?.apply {
            forEach {
                filePath.add(if (it.type == ImageRes.TYPE_VIDEO) it.previewPath!! else it.path)
            }
        }
        mViewModel.publishPost(
            Post(
                postType = PostType.IMAGE_TEXT,
                textContent = content
            ), filePath
        )
    }


    private fun getImageData(): List<ImageRes>? {
        return when (mCurrentMode) {
            MODE_IMAGE -> {
                mImageView.getData()
            }
            MODE_VIDEO -> {
                mVideoView.getData()
            }
            else -> null
        }
    }

    private fun showContent(mode: Int) {
        mCurrentMode = mode
        when (mode) {
            MODE_ADD -> {
                content_container.removeAllViews()
                content_container.addView(mAddView)
            }
            MODE_VIDEO -> {
                content_container.removeAllViews()
                content_container.addView(mVideoView)
            }
            MODE_IMAGE -> {
                content_container.removeAllViews()
                content_container.addView(mImageView)
            }
        }
    }


    override fun initData() {
        mViewModel
    }

    override fun startObserve() {
        mViewModel.liveData.observe(this, Observer {
            if (it.showLoading) {
                showLoading()
            } else {
                dismissLoading()
            }

            it.showError?.apply { toast(this) }

            if (it.isSuccess) {
                toast("帖子发表成功，审核通过后别人就可以看到啦")
                finish()
            }

        })
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, ContentPublishActivity::class.java)
            context.startActivity(intent)
        }
    }
}