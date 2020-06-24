package com.youloft.senior.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.net.bean.CommentData
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
class CommentAdapterr(data: MutableList<CommentData>?) :
    BaseQuickAdapter<CommentData, BaseViewHolder>(R.layout.item_comment, data) {
    override fun convert(holder: BaseViewHolder, item: CommentData) {
        holder.setText(R.id.tv_name, item.nickname).setText(R.id.tv_content, item.content)
            .setText(R.id.tv_date, item.createTime)
    }
}