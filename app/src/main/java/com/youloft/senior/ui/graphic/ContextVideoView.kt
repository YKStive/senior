package com.youloft.senior.ui.graphic

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.context_video.view.*

/**
 * @author you
 * @create 2020/6/29
 * @desc
 */
class ContextVideoView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {
    private lateinit var mItem: ImageRes
    val emptyData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val inflate = LayoutInflater.from(context).inflate(R.layout.context_video, this)
        inflate.apply {
            iv_delete.setOnClickListener {
                emptyData.value = true
            }

            player.apply {
                backButton.visibility = View.GONE
                titleTextView.visibility = View.GONE

            }
        }
    }

    fun setVideo(imageRes: ImageRes) {
        this.mItem = imageRes
        GSYVideoOptionBuilder()
            .setUrl(imageRes.previewPath)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    "prepare".logD()
                }
            })
            .build(player)

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        player.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
    }

    fun getData(): List<ImageRes>? {
        return listOf(mItem)
    }


}