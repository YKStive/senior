package com.youloft.senior.ui.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.youloft.core.base.BaseVMFragment
import com.youloft.net.bean.MineData
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.MineItemAdapter
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 我的 列表三种类型，影集 图文照片 GIF
 */
class MainFragment : BaseVMFragment() {
    private val mViewModel by viewModels<MainViewModel>()
    private val mAdapter: MineItemAdapter = MineItemAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.adapter=mAdapter
    }

    override fun initData() {
        var data=mutableListOf<MineData>()
        for (i in 0..10){
            data.add(MineData("2020","3", mutableListOf(),2,55,"ssss",3,"koskofkodkodkodk"
                ,455,48848))
        }
        mAdapter.setList(data)
//mViewModel
    }

    override fun startObserve() {

    }

}