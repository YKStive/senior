package com.youloft.senior.ui.graphic

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.CancellationSignal
import android.util.AttributeSet
import android.util.Size
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
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.context_video.view.*
import java.io.File


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

        }
    }

    fun setVideo(imageRes: ImageRes) {
        val videoSize = player.setVideo(imageRes.previewPath!!)
        mItem = imageRes
        mItem.apply {
            this.videoWidth = videoSize[0]
            this.videoHeight = videoSize[1]
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        player.clear()

    }

    fun getData(): List<ImageRes>? {
        return listOf(mItem)
    }


}