package com.youloft.senior.ui.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.youloft.net.bean.MineData
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
class MineItemAdapter(val params: (String) -> Unit) : BaseProviderMultiAdapter<MineData>() {
    init {
        addItemProvider(TextAndImageItem(params))
        addItemProvider(VideoItem(params))
        println("Adapter CB ${params.hashCode().toString()}")
        addItemProvider(MovieItem {
            params(it)
        })
        addItemProvider(GifItem(params))
    }


    override fun getItemType(data: List<MineData>, position: Int): Int {
        return data.get(position).postType
    }


}