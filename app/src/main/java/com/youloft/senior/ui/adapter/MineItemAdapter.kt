package com.youloft.senior.ui.adapter

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.youloft.net.bean.MineData


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
class MineItemAdapter : BaseProviderMultiAdapter<MineData>() {
    init {
        addItemProvider(TextAndImageItem())
        addItemProvider(VideoItem())
        addItemProvider(MovieItem())
        addItemProvider(GifItem())
    }


    override fun getItemType(data: List<MineData>, position: Int): Int {
        return data.get(position).postType
    }


}