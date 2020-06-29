package com.youloft.senior.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.net.bean.CommentBean
import com.youloft.senior.R


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/23 9:58
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 9:58
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class CommentAdapterr(data: MutableList<CommentBean>?) :
    BaseQuickAdapter<CommentBean, BaseViewHolder>(R.layout.item_comment, data) , LoadMoreModule{
    init {
        addChildClickViewIds(R.id.ll_favorite)
    }
    override fun convert(holder: BaseViewHolder, item: CommentBean) {
//        holder.setText(R.id.tv_name, item.nickname).setText(R.id.tv_content, item.content)
//            .setText(R.id.tv_date, item.createTime)
//
        Glide.with(context).load(item.avatar).into(holder.getView(R.id.img_head))
        holder.setText(R.id.tv_name, item.nickname).setText(R.id.tv_content, item.content)
            .setText(R.id.tv_date, item.createTime)

    }

//    override fun getItemCount(): Int {
//        return 20
//    }
}