package com.youloft.senior.ui.gif

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
        btn_resource.setOnClickListener {
            ChoiceImageActivity.start(this) {
                Glide.with(this).load(it.path).into(iv_resource)
                resource = BitmapFactory.decodeFile(it.path)
            }
        }

        btn_temp.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val submit =
                        Glide.with(applicationContext).asGif().load(R.drawable.test).submit()
                    temp = submit.get()
                }

                withContext(Dispatchers.Main) {
                    iv_temp.setImageDrawable(temp)
                }
            }
        }

        composite.setOnClickListener {
            val gifMaker = GifMaker()
            composite.setText("合成中...")
            gifMaker.makeNeGif(applicationContext, temp, resource) {
                composite.setText("合成成功")
                Glide.with(this).load(it).into(iv_target)
            }
        }


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