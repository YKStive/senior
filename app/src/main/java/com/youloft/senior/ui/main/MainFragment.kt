package com.youloft.senior.ui.main

import com.youloft.core.base.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author you
 * @create 2020/6/18
 * @desc 信息流界面
 */
class MainFragment : BaseVMFragment<MainViewModel>() {
    override fun getLayoutResId(): Int {
        return 0
    }

    override fun initVM(): MainViewModel = getViewModel()


    override fun initView() {

    }

    override fun initData() {
    }

    override fun startObserve() {

    }

}