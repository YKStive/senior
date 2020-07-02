package com.youloft.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.youloft.core.Analytics

/**
 * @author you
 * @create 2020/6/18
 * @desc mvvm activity 基类
 */
abstract class BaseVMActivity :
    AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startObserve()
        setContentView(getLayoutResId())
        initView()
        initData()
    }

    open fun getLayoutResId(): Int = 0
    abstract fun initView()
    abstract fun initData()
    abstract fun startObserve()

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