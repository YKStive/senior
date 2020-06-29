package com.youloft.senior.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseFragment
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.net.NetResponse
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie_detail.iv_head
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_browse_number
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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
class MovieAndGifDetailFragment : BaseFragment() {
    //    private val mViewModel by viewModels<MovieViewModel>()
    var type = MineDataBean.GIF_TYPE;
    var id: String = ""

    //单挑帖子详情 json
    var mItemInfo: String? = ""

    companion object {
        //        const val isGif = 1;
//        const val isMovie = 2;
        fun newInstance(type: Int, mItemInfo: String, id: String): MovieAndGifDetailFragment {
            val args = Bundle()
            args.putInt("type", type)
            args.putString("itemInfo", mItemInfo)
            args.putString("id", id)
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
            var itemId = it.getString("id")
            if (!itemId.isNullOrBlank()) {
                id = itemId
            }
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
        getDetailData(id)
    }

    fun getDetailData(id: String) {
        lifecycleScope.launchIOWhenCreated({
            it.message?.logD()
        }, {
//            val stickers = ApiHelper.api.getStickers()
            val stickers = NetResponse<ItemData>(ApiHelper.api.getItem(id).data, "", "", 200)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(stickers, {
                    activity?.let { it1 -> Glide.with(it1).load(it?.avatar).into(iv_head) }
                    tv_name.setText(it.nickname)
                    tv_browse_number.setText("${it.viewed}次浏览")
                    tv_content.setText(it.textContent)
                    if (type == MineDataBean.GIF_TYPE) {

                    } else if (type == MineDataBean.MOVIE_TYPE) {

                    }
//                    detailPlayer.setUp(it.mediaContent[0], true, "")
                    tv_create_time_movie_gif.setText(it.createTime)

                })
            }
        })
    }
}