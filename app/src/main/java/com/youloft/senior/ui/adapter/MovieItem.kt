package com.youloft.senior.ui.adapter

import android.view.View
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean


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
class MovieItem(
    val itemClick: (id: String, type: Int) -> Unit,
    val itemShare: (id: String, type: Int) -> Unit,
    val itemFavorite: (id: String, type: Int) -> Unit
) : BaseItemProvider<MineDataBean>() {
    override val itemViewType: Int
        get() = MineDataBean.MOVIE_TYPE
    override val layoutId: Int
        get() = R.layout.item_movie

    init {
        addChildClickViewIds(R.id.tv_share, R.id.tv_praise)
    }

    override fun convert(helper: BaseViewHolder, item: MineDataBean) {
        helper.setText(R.id.tv_browse_number, item.createTime).setText(
            R.id.tv_content
            , item.textContent
        ).setText(R.id.tv_praise, item.praised.toString())
    }

    override fun onChildClick(
        helper: BaseViewHolder,
        view: View,
        data: MineDataBean,
        position: Int
    ) {
        when (view.id) {
            R.id.tv_share -> itemShare(data.id, itemViewType)
            R.id.tv_praise -> itemFavorite(data.id, itemViewType)
        }

    }

    override fun onClick(helper: BaseViewHolder, view: View, data: MineDataBean, position: Int) {
        itemClick(data.id, itemViewType)

    }
}