package com.youloft.senior.ui.home

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R

/**
 * @author you
 * @create 2020/6/18
 * @desc 信息流界面
 */
class HomeFragment : BaseVMFragment<HomeViewModel>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

    }

    override fun initData() {
    }

    override fun startObserve() {

    }

    override fun initVM(): HomeViewModel {
        return HomeViewModel()
    }

}