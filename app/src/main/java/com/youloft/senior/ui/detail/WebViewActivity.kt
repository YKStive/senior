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
import com.youloft.senior.bean.MakeMovieBean
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.ui.gif.PostPublishActivity
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

    //如果是制作影集则需要图片path
    var imges: String = ""

    //如果是制作影集则需要影集模板id
    var movieTemplateId: String = ""
    var currentWebType = MOVIE_WEB_FULLSCREEN
    var imagesPathList: List<String> = arrayListOf()

    override fun getLayoutResId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        val webSettings: WebSettings = web_movie.getSettings()
        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true
        image_back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        url = intent.getStringExtra("url")
        imges = intent.getStringExtra("imges");
        movieTemplateId = intent.getStringExtra("movieTemplateId");
        currentWebType = intent.getIntExtra("type", MOVIE_WEB_FULLSCREEN);
        if (currentWebType == MOVIE_WEB_FULLSCREEN) {
            tv_exit.visibility = View.VISIBLE
            tv_exit.setOnClickListener {
                finish()
            }
        } else if (currentWebType == MOVIE_WEB_PREVIEW) {
            tv_publish.visibility = View.VISIBLE
            tv_publish.setOnClickListener {
                //TODO
                PostPublishActivity.start(
                    this,
                    Post(
                        postType = PostType.ALBUM,
                        mediaContent = imagesPathList,
                        templateId = movieTemplateId
                    )
                ) {
                    finish()
                }
            }
        }
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
                var schema = "data:image/png;base64,"
                arrarList?.let {
                    for (index in 0..arrarList.lastIndex) {
                        val imageName = arrarList.get(index)?.path
                        //todo 添加路径
//                        imagesPathList.add(imageName)
                        imageName?.let {
                            if (imageName.indexOf(".") > 0) {
                                if (imageName.contains(".jpg")) {
                                    schema = "data:image/jpg;base64,"
                                }
                            }
                        }
                        var base64Str = com.youloft.util.Base64.encodeFromFile(imageName)
                        base64Str?.let {
                            imageList.add(schema + base64Str)
                        }
                    }
                    val h5NeedParams = MakeMovieBean(movieTemplateId, imageList)
                    val imageRes = h5NeedParams.toJsonString()
                    imageRes.logE(TAG)
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
        fun start(
            context: Context,
            url: String,
            imges: String?,
            currentWebType: Int,
            movieTemplateId: String?
        ) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra("url", url)
            starter.putExtra("imges", imges)
            starter.putExtra("type", currentWebType)
            starter.putExtra("movieTemplateId", movieTemplateId)
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

    /**
     * 将图片转换成Base64编码的字符串
     */
//    open fun imgToBase64String(url: String?): String? {
//        var url = url ?: return null
//        val bitmap: Bitmap = BitmapFactory.decodeFile(url)
//        val stream = ByteArrayOutputStream()
//        if (url.indexOf(".") > 0) {
//            if (url.contains(".png")) {
//                url = url.substring(0, url.indexOf(".png"))
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            } else if (url.contains(".jpg")) {
//                url = url.substring(0, url.indexOf(".jpg"))
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//            }
//        }
//        val byteServer: ByteArray = stream.toByteArray()
//        try {
//            stream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return Base64.encodeToString(byteServer, 0, byteServer.size, Base64.DEFAULT)
//    }

}