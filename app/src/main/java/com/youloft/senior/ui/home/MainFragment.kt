package com.youloft.senior.ui.home

import androidx.lifecycle.Observer
import com.youloft.core.base.BaseVMFragment
import com.youloft.net.ApiHelper
import com.youloft.senior.R

/**
 * @author you
 * @create 2020/6/18
 * @desc 我的
 */
class MainFragment : BaseVMFragment<HomeViewModel>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_main
    }

    override fun initVM(): HomeViewModel {
        return HomeViewModel()
    }


    override fun initView() {

    }

    override fun initData() {

    }

    override fun startObserve() {
        mViewModel.data.observe(this, Observer {

        })
    }

}