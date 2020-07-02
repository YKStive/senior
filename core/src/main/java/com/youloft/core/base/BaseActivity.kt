package com.youloft.core.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.youloft.core.Analytics

/**
 * @author you
 * @create 2020/6/18
 * @desc activity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView()
        initData()
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()
    fun showLoading() {}
    fun dismissLoading() {}

    override fun onResume() {
        super.onResume()
        Analytics.postActivityResume(this)
    }

    override fun onPause() {
        super.onPause()
        Analytics.postActivityPause(this)
    }
}