package com.youloft.senior.ui.adapter

import android.view.View
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.net.bean.MineData
import com.youloft.net.bean.MineDataBean
import com.youloft.senior.R


/**
 *
 * @Description:     我的列表 视频类型item
 * @Author:         slh
 * @CreateDate:     2020/6/23 15:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 15:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class VideoItem(params: (String) -> Unit) : BaseItemProvider<MineData>() {
    override val itemViewType: Int
        get() = MineData.VIDEO_TYPE
    override val layoutId: Int
        get() = R.layout.item_video

    override fun convert(helper: BaseViewHolder, item: MineData) {
    }

    override fun onChildClick(helper: BaseViewHolder, view: View, data: MineData, position: Int) {
        super.onChildClick(helper, view, data, position)
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: MineData, position: Int) {
        super.onClick(helper, view, data, position)
    }


}