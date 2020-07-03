package com.youloft.senior.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R
import com.youloft.senior.bean.MovieTemplateBean


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/7/3 10:58
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/3 10:58
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MovieTemplateAdapter :
    BaseQuickAdapter<MovieTemplateBean, BaseViewHolder>(
        R.layout.item_movie_template
    ) {
    override fun convert(holder: BaseViewHolder, item: MovieTemplateBean) {
        Glide.with(context).load(item.thumbnail).into(holder.getView(R.id.imageview))
    }
}