package com.youloft.senior.ui.main

import com.youloft.core.base.BaseVMFragment

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class MainFragment : BaseVMFragment<MainViewModel>() {
    override fun getLayoutResId(): Int {
        return 0
    }

    override fun initVM(): MainViewModel {
        return MainViewModel()
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

}