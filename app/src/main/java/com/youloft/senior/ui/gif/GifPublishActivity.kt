package com.youloft.senior.ui.gif

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseActivity
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.R
import com.youloft.senior.bean.GifBean
import com.youloft.senior.itembinder.GifListViewBinder
import com.youloft.senior.net.Api
import com.youloft.senior.utils.ImageLoader
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.activity_gif_list.*
import kotlinx.android.synthetic.main.activity_gif_list.common_title
import kotlinx.android.synthetic.main.activity_gif_publish.*

/**
 * @author you
 * @create 2020/6/24
 * @desc gif发布
 */
class GifPublishActivity : BaseActivity() {


    private lateinit var mGifPath: String
    override fun getLayoutResId(): Int {
        return R.layout.activity_gif_publish
    }


    override fun initView() {
        common_title.onBack {
            finish()
        }

        tv_publish.setOnClickListener {
            val content = et_content.text
            if (content == null) {
                toast("说点什么吧！")
            } else {
                //todo 发表影集 detail singleTask
            }
        }

    }

    override fun initData() {
        mGifPath = intent.getStringExtra(GIF_PATH)!!
        ImageLoader.loadImage(this@GifPublishActivity, mGifPath, iv_gif)

    }


    companion object {
        private const val GIF_PATH = "gif_path"
        fun start(context: FragmentActivity, gifPath: String) {
            val intent = Intent(context, GifPublishActivity::class.java)
            intent.putExtra(GIF_PATH, gifPath)
            context.startActivity(intent)
        }
    }

}