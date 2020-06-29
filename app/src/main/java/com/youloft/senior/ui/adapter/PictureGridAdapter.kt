package com.youloft.senior.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R


/**
 *
 * @Description:     java类作用描述
 * @Author:         slh
 * @CreateDate:     2020/6/29 14:27
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/29 14:27
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class PictureGridAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.imageview) {
    override fun convert(holder: BaseViewHolder, item: String) {
        Glide.with(context).load(item).into(holder.getView(R.id.image))
    }
}