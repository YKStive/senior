package com.youloft.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author you
 * @create 2020/6/18
 * @desc activity基类，mvvm
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
}