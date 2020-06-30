package com.youloft.senior.utils

import androidx.recyclerview.widget.DiffUtil
import com.youloft.net.bean.CommentBean


/**
 *
 * @Description:    判断评论列表内容是否变化
 * @Author:         slh
 * @CreateDate:     2020/6/30 17:44
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/30 17:44
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class DiffCommentCallback : DiffUtil.ItemCallback<CommentBean>() {
    override fun areItemsTheSame(oldItem: CommentBean, newItem: CommentBean): Boolean {
        return oldItem.id.equals(newItem.id)
    }

    override fun areContentsTheSame(oldItem: CommentBean, newItem: CommentBean): Boolean {
        return oldItem.praised == newItem.praised
    }

}