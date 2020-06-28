package com.youloft.senior.ui.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean


/**
 *
 * @Description:     我的列表 gif类型item
 * @Author:         slh
 * @CreateDate:     2020/6/23 15:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 15:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class GifItem(
    val itemClick: (id: String, type: Int) -> Unit,
    val itemShare: (id: String, type: Int) -> Unit,
    val itemFavorite: (id: String, type: Int) -> Unit
) : BaseItemProvider<MineDataBean>() {
    override val itemViewType: Int
        get() = MineDataBean.GIF_TYPE
    override val layoutId: Int
        get() = R.layout.item_gif

    init {
        addChildClickViewIds(
            R.id.tv_share, R.id.tv_praise

        )
    }

    override fun onChildClick(helper: BaseViewHolder, view: View, data: MineDataBean, position: Int) {
        super.onChildClick(helper, view, data, position)
        when (view.id) {
            R.id.tv_share -> itemShare(data.id, itemViewType)
            R.id.tv_praise -> itemFavorite(data.id, itemViewType)
        }

    }

    override fun convert(helper: BaseViewHolder, item: MineDataBean) {
        helper.setText(R.id.tv_browse_number, item.createTime).setText(
            R.id.tv_content
            , item.textContent
        ).setText(R.id.tv_praise, item.praised.toString())
        Glide.with(context).load(item.mediaContent?.get(0)).into(helper.getView(R.id.img_gif))
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: MineDataBean, position: Int) {
        super.onClick(helper, view, data, position)
        itemClick(data.id, itemViewType)
    }

}