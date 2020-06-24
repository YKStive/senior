package com.youloft.senior.ui.gif

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.utils.gifmaker.GifMaker
import kotlinx.android.synthetic.main.activity_gif_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.AccessControlContext

/**
 * @author you
 * @create 2020/6/19
 * @desc
 */
class GifActivity : BaseActivity() {
    private lateinit var resource: Bitmap
    lateinit var temp: GifDrawable
    override fun getLayoutResId(): Int {
        return R.layout.activity_gif_list
    }

    override fun initView() {


    }

    override fun initData() {
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, GifActivity::class.java)
            context.startActivity(intent)
        }
    }

}