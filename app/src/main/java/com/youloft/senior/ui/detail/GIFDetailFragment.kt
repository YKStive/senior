package com.youloft.senior.ui.detail

import android.os.Bundle
import com.youloft.core.base.BaseFragment
import com.youloft.senior.R
import kotlinx.android.synthetic.main.fragment_movie_detail.*


/**
 *
 * @Description:     影集详情
 * @Author:         slh
 * @CreateDate:     2020/6/23 17:21
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 17:21
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class GIFDetailFragment : BaseFragment() {
    companion object{
        fun newInstance(): GIFDetailFragment {
            val args = Bundle()
            val fragment = GIFDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_movie_detail
    }

    override fun initView() {
        web_movie.loadUrl("https://www.baidu.com/")
    }

    override fun initData() {

    }
}