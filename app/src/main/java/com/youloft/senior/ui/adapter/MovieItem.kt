package com.youloft.senior.ui.adapter

import android.view.View
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.net.bean.MineData
import com.youloft.net.bean.MineDataBean
import com.youloft.senior.R
import com.youloft.senior.ui.detail.DetailActivity
import com.youloft.senior.utils.logD


/**
 *
 * @Description:     我的列表 影集类型item
 * @Author:         slh
 * @CreateDate:     2020/6/23 15:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 15:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MovieItem( params: (String) -> Unit) : BaseItemProvider<MineData>() {
    override val itemViewType: Int
        get() = MineData.MOVIE_TYPE
    override val layoutId: Int
        get() = R.layout.item_movie
    val wapper = params
    init {
        "MovieItem".logD()
        println("MovieItem CB ${   params.hashCode().toString()}")
        wapper.hashCode().toString().logD()
        println("raw:"+this.hashCode())
    }

    override fun convert(helper: BaseViewHolder, item: MineData) {
        helper.setText(R.id.tv_content, item.textContent)
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: MineData, position: Int) {
        super.onClick(helper, view, data, position)
        "ssss".logD()
        println("click:"+this.hashCode())
        wapper.hashCode().toString().logD()
        wapper(data.id)
        println("MovieItem Click CB ${   wapper.hashCode().toString()}")

//        DetailActivity.start()
    }
}