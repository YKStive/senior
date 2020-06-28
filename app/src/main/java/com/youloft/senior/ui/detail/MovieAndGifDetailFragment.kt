package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.MineDataBean
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
class MovieAndGifDetailFragment : BaseVMFragment() {
    private val mViewModel by viewModels<MovieViewModel>()
    var type = MineDataBean.GIF_TYPE;

    //单挑帖子详情 json
    var mItemInfo: String? = ""

    companion object {
        //        const val isGif = 1;
//        const val isMovie = 2;
        fun newInstance(type: Int, mItemInfo: String): MovieAndGifDetailFragment {
            val args = Bundle()
            args.putInt("type", type)
            args.putInt("itemInfo", type)
            val fragment = MovieAndGifDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_movie_detail
    }

    override fun initView() {
        arguments?.let {
            type = it.getInt("type", MineDataBean.GIF_TYPE)
            mItemInfo = it.getString("itemInfo")
        }

        if (type == MineDataBean.GIF_TYPE) {
            imgeview_gif.visibility = View.VISIBLE
            web_movie.visibility = View.GONE
        } else {
            imgeview_gif.visibility = View.GONE
            web_movie.visibility = View.VISIBLE
        }
        web_movie.loadUrl("https://www.baidu.com/")
    }

    override fun initData() {

        mViewModel.getItem("3")
    }

    override fun startObserve() {
//        mViewModel.itemData.observe(this, Observer {
//            if (it.isSuccess) {
//                val itemData = it.getOrNull()
//                if (itemData != null) {
////                    tv_name.text = itemData.data?.nickname
//                } else {
//                }
//            } else {
//            }
//        })
    }
}