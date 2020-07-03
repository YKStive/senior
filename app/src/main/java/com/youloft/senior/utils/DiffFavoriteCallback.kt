package com.youloft.senior.utils

import androidx.recyclerview.widget.DiffUtil
import com.youloft.net.bean.CommentBean
import com.youloft.senior.bean.FavoriteHeadBean


/**
 *
 * @Description:    判断点赞列表内容是否变化
 * @Author:         slh
 * @CreateDate:     2020/6/30 17:44
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/30 17:44
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class DiffFavoriteCallback : DiffUtil.ItemCallback<FavoriteHeadBean>() {
    override fun areItemsTheSame(oldItem: FavoriteHeadBean, newItem: FavoriteHeadBean): Boolean {
        return oldItem.userId.equals(newItem.userId)
    }

    override fun areContentsTheSame(oldItem: FavoriteHeadBean, newItem: FavoriteHeadBean): Boolean {
        return oldItem.userId == newItem.userId
    }

}