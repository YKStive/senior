package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.coolktx.toJsonString
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import kotlinx.android.synthetic.main.fragment_movie_detail.*
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
class MovieAndGifDetailFragment : BaseVMFragment() {
    //    private val mViewModel by viewModels<MovieViewModel>()
    var type = MineDataBean.GIF_TYPE;
    var id: String = ""
    var mViewModel: DetailViewModel? = null

    companion object {
        //        const val isGif = 1;
//        const val isMovie = 2;
        private const val TAG = "MovieAndGifDetailFragme"
        fun newInstance(type: Int, mItemInfo: String, id: String): MovieAndGifDetailFragment {
            val args = Bundle()
            args.putInt("type", type)
            args.putString("itemInfo", mItemInfo)
            args.putString("id", id)
            val fragment = MovieAndGifDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_movie_detail
    }

    override fun initView() {
        arguments?.let {
            type = it.getInt("type", MineDataBean.GIF_TYPE)
            val itemId = it.getString("id")
            if (!itemId.isNullOrBlank()) {
                id = itemId
            }
        }

        if (type == MineDataBean.GIF_TYPE) {
            imgeview_gif.visibility = View.VISIBLE
            framelayout_movie.visibility = View.GONE
            tv_make.setText("制作同样的表情")
        } else {
            imgeview_gif.visibility = View.GONE
            framelayout_movie.visibility = View.VISIBLE
            val webSettings: WebSettings = web_movie.getSettings()
            tv_make.setText("制作同样的影集")
            // 设置与Js交互的权限
            webSettings.javaScriptEnabled = true
            // 设置允许JS弹窗
//        webSettings.javaScriptCanOpenWindowsAutomatically = true
            web_movie.loadUrl("http://192.168.2.22:5000/preview-album?autoplay=1")
            tv_full_screen.setOnClickListener {
                activity?.let { it1 ->
                    //暂停播放
                    web_movie.post {
                        web_movie.jsBridge.executeJavaScript("javascript:pauseAlbum()")
                    }
                    WebViewActivity.start(
                        it1,
                        "http://192.168.2.22:5000/preview-album?autoplay=1",
                        "",
                        WebViewActivity.MOVIE_WEB_FULLSCREEN,
                        ""
                    )
                }
            }
        }
        tv_make.setOnClickListener {
            activity?.let { thisActivity ->
                ChoiceImageActivity.start(thisActivity, 20) {
//                    var imageList = mutableListOf<String>()
//                    for (index in 0..it.lastIndex) {
//                        imageList.add(Base64.encodeFromFile(it[index].path))
//                    }
//                    val imageRes = imageList.toJsonString()
                    val imageRes = it.toJsonString()
                    activity?.let {
                        WebViewActivity.start(
                            it,
                            "http://192.168.2.22:8080/preview-album.html?autoplay=1&id=1",
                            imageRes,
                            WebViewActivity.MOVIE_WEB_PREVIEW,
                            ""

                        )
                    }


                }
            }
        }

    }

    override fun initData() {
        getDetailData(id)
    }

    override fun startObserve() {
        mViewModel = activity?.let { ViewModelProvider(it).get(DetailViewModel::class.java) }
    }

    fun getDetailData(id: String) {
        lifecycleScope.launchIOWhenCreated({
            it.message?.logD()
        }, {
//            val stickers = ApiHelper.api.getStickers()
            val res = ApiHelper.api.getItem(id)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(res, {
                    activity?.let { it1 -> Glide.with(it1).load(it?.avatar).into(iv_head) }
                    tv_name.setText(it.nickname)
                    mViewModel?.postInfo?.value = it
                    tv_browse_number.setText("${it.viewed}次浏览")
                    tv_content.setText(it.textContent)
                    if (type == MineDataBean.GIF_TYPE) {

                    } else if (type == MineDataBean.MOVIE_TYPE) {

                    }
//                    detailPlayer.setUp(it.mediaContent[0], true, "")
                    tv_create_time_movie_gif.setText(it.createTime)

                })
            }
        })
    }

    /************************webview生命周期处理****************/
    override fun onPause() {
        if (type == MineDataBean.MOVIE_TYPE) {
            web_movie.onPause()
            web_movie.pauseTimers()
//            web_movie.addJavascriptInterface()
            "onPause".logE(TAG)
        }
        super.onPause()
    }

    override fun onResume() {
        if (type == MineDataBean.MOVIE_TYPE) {
            web_movie.post {
                web_movie.jsBridge.executeJavaScript("javascript:playAlbum()")
            }
            web_movie.onResume()
            web_movie.resumeTimers()
            "onResume".logE(TAG)
        }
        super.onResume()
    }


    override fun onDestroyView() {
        "onDestroyView".logE(TAG)
        web_movie?.let {
            web_movie.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            web_movie.clearHistory()
            with(web_movie.parent as? ViewGroup) {
                this?.removeView(web_movie)
                web_movie.destroy()
            }
        }
        super.onDestroyView()
    }

}