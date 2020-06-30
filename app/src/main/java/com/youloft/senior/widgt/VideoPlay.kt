package com.youloft.senior.widgt

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.CancellationSignal
import android.util.AttributeSet
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.youloft.coolktx.dp2px
import com.youloft.senior.base.App
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import com.youloft.util.UiUtil
import java.io.File

/**
 * @author you
 * @create 2020/6/30
 * @desc
 */
class VideoPlay(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private var mPlayer = StandardGSYVideoPlayer(context)

    init {
        mPlayer.apply {
            backButton.visibility = View.GONE
            titleTextView.visibility = View.GONE
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        addView(mPlayer)
    }

    fun setVideo(path: String, size: Size? = null): Array<Int> {
        var videoTrueWidth = UiUtil.getScreenWidth(App.instance())
        var videoTrueHeight = 200.dp2px
        if (size == null) {
            if (isLocalPath(path)) {
                val metaRetriever = MediaMetadataRetriever()
                metaRetriever.setDataSource(path)
                val videoHeight =
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                        .toInt()
                val videoWidth =
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                        .toInt()

                videoTrueWidth =
                    if (videoWidth > UiUtil.getScreenWidth(App.instance())) UiUtil.getScreenWidth(
                        App.instance()
                    ) else videoWidth
                videoTrueHeight =
                    if (videoHeight > UiUtil.getScreenHeight(App.instance())) UiUtil.getScreenHeight(
                        App.instance()
                    ) else videoHeight
            }

        } else {
            videoTrueWidth = size.width
            videoTrueHeight = size.width

        }


        val thumbView = ImageView(App.instance())
        thumbView.layoutParams = ViewGroup.LayoutParams(videoTrueWidth, videoTrueHeight)
        thumbView.scaleType = ImageView.ScaleType.CENTER_CROP

        if (isLocalPath(path)) {
            val thumbBitmap = ThumbnailUtils.createVideoThumbnail(
                File(path), Size(videoTrueWidth, videoTrueHeight),
                CancellationSignal()
            )
            thumbView.setImageBitmap(thumbBitmap)
        }


        mPlayer.apply {
            if (layoutParams == null) {
                layoutParams = ViewGroup.LayoutParams(videoTrueWidth, videoTrueHeight)
            } else {
                layoutParams.width = videoTrueWidth
                layoutParams.height = videoTrueHeight
            }
        }


        val realPath = if (isLocalPath(path)) "file://${path}" else path
        realPath.logE()
        GSYVideoOptionBuilder()
            .setThumbImageView(thumbView)
            .setIsTouchWiget(false)
            .setUrl("file:///storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1588507427887.mp4")
            .setVideoTitle("")
            .setFullHideActionBar(true)
            .setCacheWithPlay(false)
            .setRotateViewAuto(false)
            .setLockLand(true)
            .setPlayTag(path)
            .setMapHeadData(mapOf(Pair("22", "33")))
            .setShowFullAnimation(true)
            .setNeedLockFull(true)
            .build(mPlayer)

        return arrayOf(videoTrueWidth, videoTrueHeight)
    }

    fun clear() {
        mPlayer.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear()
    }

    fun isLocalPath(path: String): Boolean {
        return !path.startsWith("http")
    }
}