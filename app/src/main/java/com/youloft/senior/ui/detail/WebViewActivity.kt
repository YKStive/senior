package com.youloft.senior.ui.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.utils.logE
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
    var imges: String? = ""
    override fun getLayoutResId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        val webSettings: WebSettings = web_movie.getSettings()
        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true
        tv_exit.visibility = View.VISIBLE
        image_back.setOnClickListener {
            finish()
        }
        tv_exit.setOnClickListener {
            finish()
        }

    }

    override fun initData() {
        url = intent.getStringExtra("url")
        imges = intent.getStringExtra(imges);
        if (url.isNotEmpty()) {
            web_movie.loadUrl(url)
        }
        if (imges?.isNotBlank() != null) {
            if (imges!!.isNotEmpty()) {
                web_movie.post {
                    web_movie.jsBridge.executeJavaScript("javascript:getAlbumData(${imges})")
                }
            }
        }
    }

    companion object {
        private const val TAG = "WebViewActivity"

        @JvmStatic
        fun start(context: Context, url: String, imges: String?) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra("url", url)
            starter.putExtra("imges", imges)

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