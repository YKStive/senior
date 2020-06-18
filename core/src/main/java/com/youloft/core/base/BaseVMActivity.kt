package com.youloft.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author you
 * @create 2020/6/18
 * @desc mvvm activity 基类
 */
abstract class BaseVMActivity<VM : BaseViewModel> :
    AppCompatActivity() {

    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = initVM()

        startObserve()
        setContentView(getLayoutResId())
        initView()
        initData()
    }

    open fun getLayoutResId(): Int = 0
    abstract fun initVM(): VM
    abstract fun initView()
    abstract fun initData()
    abstract fun startObserve()

}