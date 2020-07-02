package com.youloft.senior.ui.detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseFragment
import com.youloft.senior.R
import com.youloft.senior.bean.ItemData
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.net.NetResponse
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie_detail.iv_head
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_browse_number
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_name
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 *
 * @Description:     影集详情
 * @Author:         slh
 * @CreateDate:     2020/6/23 17:21
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 17:21
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class GIFDetailFragment : BaseFragment() {
    var url = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4"
    lateinit var orientationUtils: OrientationUtils
    private var isPlay = false
    private var isPause = false
    lateinit var detailPlayer: StandardGSYVideoPlayer;
    var id: String = ""//帖子id
//    var detailPlayer: StandardGSYVideoPlayer? = null

    companion object {
        fun newInstance(id: String): GIFDetailFragment {
            val args = Bundle()
            args.putString("id", id);
            val fragment = GIFDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_video
    }

    override fun initView() {
        detailPlayer = activity?.findViewById(R.id.detail_player) as StandardGSYVideoPlayer
    }

    override fun initData() {
        var itemId = arguments?.getString("id")
        if (!itemId.isNullOrEmpty()) {
            id = itemId
        }
        getDetailData(id)
        //增加title
        detailPlayer.titleTextView.visibility = View.GONE
        detailPlayer.backButton.visibility = View.GONE
        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(activity, detail_player)
//初始化不打开外部的旋转
        orientationUtils.setEnable(false)

        val gsyVideoOption = GSYVideoOptionBuilder()
        //.setThumbImageView(imageView)
        gsyVideoOption
            .setRotateViewAuto(false)
            .setThumbPlay(true)
            .setIsTouchWiget(false)
            .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setUrl(url)
            .setCacheWithPlay(false)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String, vararg objects: Any) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils.setEnable(true)
                    isPlay = true
                }

                override fun onQuitFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //当前非全屏player
                    if (orientationUtils != null) {
                        orientationUtils.backToProtVideo()
                    }
                }
            }).setLockClickListener { view, lock ->
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock)
                }
            }.build(detail_player)

        detailPlayer.fullscreenButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //直接横屏
                orientationUtils.resolveByClick()

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(activity, true, true)
            }
        })
    }

    fun getDetailData(id: String) {
        lifecycleScope.launchIOWhenCreated({
            it.message?.logD()
        }, {
//            val stickers = ApiHelper.api.getStickers()
            val stickers = NetResponse<ItemData>(ApiHelper.api.getItem(id).data, "", "", 200)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(stickers, {
                    activity?.let { it1 -> Glide.with(it1).load(it?.avatar).into(iv_head) }
                    tv_name.setText(it.nickname)
                    tv_browse_number.setText("${it.viewed}次浏览")
                    tv_content_video.setText(it.textContent)
//                    detailPlayer.setUp(it.mediaContent[0], true, "")
                    tv_create_time.setText(it.createTime)
//                    GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3)
                })
            }
        })
    }


    /********************************视频播放生命周期************************************************/

    fun onBackPressed(): Boolean {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo()
        }
        return GSYVideoManager.backFromWindowFull(activity)
    }
//    override fun onBackPressed() {
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo()
//        }
//        if (GSYVideoManager.backFromWindowFull(activity)) {
//            return
//        }
//        super.onBackPressed()
//    }


    override fun onPause() {
        detailPlayer.currentPlayer.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        detailPlayer.currentPlayer.onVideoResume(false)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            detailPlayer.currentPlayer?.release()
        }
        if (orientationUtils != null) orientationUtils.releaseListener()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(
                activity,
                newConfig,
                orientationUtils,
                true,
                true
            )
        }
    }
}