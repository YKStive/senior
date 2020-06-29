package com.youloft.senior.ui.graphic

import android.content.Intent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.launchIOWhenStarted
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.ui.gif.ChoiceImageActivity.Companion.TYPE_ALL
import kotlinx.android.synthetic.main.activity_content_publish.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author you
 * @create 2020/6/29
 * @desc
 */
class ContentPublishActivity : BaseVMActivity() {
    private var mCurrentMode: Int = 0
    private val imageCountLimit = 20
    private val MODE_ADD = 0
    private val MODE_VIDEO = 1
    private val MODE_IMAGE = 2
    private val mImageView by lazy {
        ContextImageView(this, null).apply {
            emptyData.observe(this@ContentPublishActivity, Observer {
                if (it) {
                    showContent(MODE_ADD)
                }
            })

            imageData.observe(this@ContentPublishActivity, Observer {
                if (it.size >=4) {
                    //todo  影集提示弹窗
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
        common_title.onBack { finish() }

        tv_publish.setOnClickListener {
            val text = et_content.text
            val imageData = getImageData()
            if (text.isNullOrEmpty() && imageData == null) {
                toast("不能都为空")
                return@setOnClickListener
            }

            publish(text.toString(), imageData)
        }

    }

    /**
     * 发布
     * @param toString String
     * @param imageData List<ImageRes>
     */
    private fun publish(content: String, imageData: List<ImageRes>?) {
        lifecycleScope.launchIOWhenStarted(onError = {
            toast("上传错误")
        }) {
            var fileRemoteResult: List<String> = listOf()
            imageData?.apply {
                //todo  图片视频上传
                val mutableListOf = mutableListOf<String>()
                imageData.forEach {
                    mutableListOf.add(if (it.type == ImageRes.TYPE_VIDEO) it.previewPath!! else it.path)
                }
                fileRemoteResult = uploadFile(mutableListOf)
            }

            val post =
                Post(
                    postType = PostType.IMAGE_TEXT,
                    textContent = content,
                    mediaContent = fileRemoteResult
                )
            val result = ApiHelper.api.publishPost(post)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(result, {
                    //todo 上传成功

                })
            }


        }
    }

    /**
     * 上传文件
     * @param mutableListOf MutableList<String>
     * @return List<String>?
     */
    private fun uploadFile(mutableListOf: MutableList<String>): List<String> {
        return arrayListOf()
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
    }

    override fun startObserve() {
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, ContentPublishActivity::class.java)
            context.startActivity(intent)
        }
    }
}