package com.youloft.senior.ui.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youloft.coolktx.toJsonString
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.utils.logE
import com.youloft.util.Base64
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.conmon_title.*


/**
 *
 * @Description:   全屏的webview
 * @Author:         slh
 * @CreateDate:     2020/7/1 16:12
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/1 16:12
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class WebViewActivity : BaseActivity() {
    var url = ""
    var imges: String = ""
    var currentWebType = MOVIE_WEB_FULLSCREEN

    override fun getLayoutResId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        val webSettings: WebSettings = web_movie.getSettings()
        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true
        if (currentWebType == MOVIE_WEB_FULLSCREEN) {
            tv_exit.visibility = View.VISIBLE
            tv_exit.setOnClickListener {
                finish()
            }
        } else if (currentWebType == MOVIE_WEB_PREVIEW) {
            tv_publish.visibility = View.VISIBLE
            tv_publish.setOnClickListener {
                //TODO
            }
        }

        image_back.setOnClickListener {
            finish()
        }


    }

    override fun initData() {
        url = intent.getStringExtra("url")
        imges = intent.getStringExtra("imges");
        currentWebType = intent.getIntExtra("type", MOVIE_WEB_FULLSCREEN);
        if (url.isNotEmpty()) {
            web_movie.loadUrl(url)
        }
        if (imges.isNotBlank()) {
            Thread(Runnable {
                var arrarList = Gson().fromJson(
                    imges,
                    object :
                        TypeToken<List<ImageRes>>() {}.type
                ) as List<ImageRes?>
                var imageList = mutableListOf<String>()
                arrarList?.let {
                    for (index in 0..arrarList.lastIndex) {
                        var imageName = arrarList?.get(index)?.path
                        imageList.add(Base64.encodeFromFile(imageName))
                    }
                    val imageRes = imageList.toJsonString()
                    web_movie.jsBridge.executeJavaScript("javascript:getAlbumData('${imageRes}')")
                }
            }).start()

        }
    }

    companion object {
        private const val TAG = "WebViewActivity"
        const val MOVIE_WEB_PREVIEW = 1
        const val MOVIE_WEB_FULLSCREEN = 0

        @JvmStatic
        fun start(context: Context, url: String, imges: String?, currentWebType: Int) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra("url", url)
            starter.putExtra("imges", imges)
            starter.putExtra("type", currentWebType)
            context.startActivity(starter)
        }
    }

    /************************webview生命周期处理****************/
    override fun onPause() {
        web_movie.onPause()
        web_movie.pauseTimers()
//            web_movie.addJavascriptInterface()
        "onPause".logE(TAG)
        //暂停播放
        web_movie.post {
            web_movie.jsBridge.executeJavaScript("javascript:stopAlbum()")
        }

        super.onPause()
    }

    override fun onResume() {
        web_movie.post {
            web_movie.jsBridge.executeJavaScript("javascript:playAlbum()")
        }
        web_movie.onResume()
        web_movie.resumeTimers()
        "onResume".logE(TAG)
        super.onResume()
    }


    override fun onDestroy() {
        "onDestroy".logE(TAG)
        web_movie?.let {
            web_movie.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            web_movie.clearHistory()
            with(web_movie.parent as? ViewGroup) {
                this?.removeView(web_movie)
                web_movie.destroy()
            }
        }
        super.onDestroy()
    }
}