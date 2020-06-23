package com.youloft.senior.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R


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
class FavoriteAdapter( data: MutableList<Any>?) :
    BaseQuickAdapter<Any, BaseViewHolder>(R.layout.item_favorite, data) {

    override fun convert(holder: BaseViewHolder, item: Any) {
    }
}