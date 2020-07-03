package com.youloft.senior.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R
import com.youloft.senior.bean.FavoriteHeadBean


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/23 10:31
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 10:31
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class FavoriteAdapter(data: MutableList<FavoriteHeadBean>?) :
    BaseQuickAdapter<FavoriteHeadBean, BaseViewHolder>(R.layout.item_favorite, data),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: FavoriteHeadBean) {
        Glide.with(context).load(item.avatar).into(holder.getView(R.id.image_head))
        holder.setText(R.id.tv_name,item.nickname)
        holder.setText(R.id.tv_favorite_time,item.createTime)
    }
}