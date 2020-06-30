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
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.youloft.senior.base.App
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.context_video.view.*
import java.io.File
import kotlin.properties.Delegates

/**
 * @author you
 * @create 2020/6/30
 * @desc
 */
class VideoPlay(context: Context, attributeSet: AttributeSet?=null) : FrameLayout(context, attributeSet) {

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

    fun setVideo(path: String): Array<Int> {

        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(path)
        var videoHeight =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
        var videoWidth =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()


        videoWidth =
            if (videoWidth > UiUtil.getScreenWidth(App.instance())) UiUtil.getScreenWidth(App.instance()) else videoWidth
        videoHeight =
            if (videoHeight > UiUtil.getScreenHeight(App.instance())) UiUtil.getScreenHeight(App.instance()) else videoHeight

        val thumbBitmap = ThumbnailUtils.createVideoThumbnail(
            File(path), Size(videoWidth, videoHeight),
            CancellationSignal()
        )

        val thumbView = ImageView(App.instance())
        thumbView.layoutParams = ViewGroup.LayoutParams(videoWidth, videoHeight)
        thumbView.scaleType = ImageView.ScaleType.CENTER_CROP
        thumbView.setImageBitmap(thumbBitmap)


        player.apply {
            layoutParams.width = videoWidth
            layoutParams.height = videoHeight
        }


        GSYVideoOptionBuilder()
            .setUrl(path)
            .setThumbImageView(thumbView)
            .build(mPlayer)

        return arrayOf(videoWidth, videoHeight)
    }

    fun clear() {
        mPlayer.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear()
    }
}