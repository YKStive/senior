package com.youloft.senior.ui.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.utils.logD


/**
 *
 * @Description:     我的页面多类型布局
 * @Author:         slh
 * @CreateDate:     2020/6/23 14:59
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 14:59
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MineItemAdapter(
    val itemClick: (id: String, type: Int) -> Unit,
    val itemShare: (id: String, type: Int) -> Unit,
    val itemFavorite: (id: String, type: Int) -> Unit,
    val imageClick: (posi: Int, imgageList: List<String>?) -> Unit
) : BaseProviderMultiAdapter<MineDataBean>() {
    init {
        addItemProvider(TextAndImageItem(itemClick, itemShare, itemFavorite, imageClick))
        addItemProvider(VideoItem(itemClick, itemShare, itemFavorite))
        addItemProvider(MovieItem(itemClick, itemShare, itemFavorite))
        addItemProvider(GifItem(itemClick, itemShare, itemFavorite))
    }

    override fun getItemType(data: List<MineDataBean>, position: Int): Int {
        return data.get(position).postType
    }


}