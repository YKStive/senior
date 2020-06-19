package com.youloft.senior.ui.main

import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.ui.gif.GifActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class MainActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_main

    }

    override fun initView() {
        btn_gif.setOnClickListener {
            GifActivity.start(this)
        }
    }

    override fun initData() {
    }
}