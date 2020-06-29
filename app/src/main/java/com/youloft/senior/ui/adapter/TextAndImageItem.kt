package com.youloft.senior.ui.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean


/**
 *
 * @Description:     我的列表 图文类型item
 * @Author:         slh
 * @CreateDate:     2020/6/23 15:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 15:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class TextAndImageItem(
    val itemClick: (id: String, type: Int) -> Unit,
    val itemShare: (id: String, type: Int) -> Unit,
    val itemFavorite: (id: String, type: Int) -> Unit,
    val imageClick: (posi: Int, imgageList: List<String>?) -> Unit
) :
    BaseItemProvider<MineDataBean>() {
    override val itemViewType: Int
        get() = MineDataBean.IMAGE_TYPE
    override val layoutId: Int
        get() = R.layout.item_mine_img_text

    init {
        addChildClickViewIds(
            R.id.tv_share, R.id.tv_praise, R.id.iv1,
            R.id.iv2,
            R.id.iv3,
            R.id.iv4,
            R.id.iv5,
            R.id.iv6,
            R.id.iv7,
            R.id.iv8,
            R.id.iv9
        )
    }

    override fun convert(helper: BaseViewHolder, item: MineDataBean) {
        helper.setText(R.id.tv_browse_number, item.createTime).setText(
            R.id.tv_content
            , item.textContent
        ).setText(R.id.tv_praise, item.praised.toString())
        var ivList = mutableListOf<ImageView>()
        with(helper) {
            ivList.add(getView(R.id.iv1))
            ivList.add(getView(R.id.iv2))
            ivList.add(getView(R.id.iv3))
            ivList.add(getView(R.id.iv4))
            ivList.add(getView(R.id.iv5))
            ivList.add(getView(R.id.iv6))
            ivList.add(getView(R.id.iv7))
            ivList.add(getView(R.id.iv8))
            ivList.add(getView(R.id.iv9))
        }
        var valideIv = mutableListOf<ImageView>()
        if (item.mediaContent?.size!! < ivList.size) {
            for (i in 0..item.mediaContent?.lastIndex) {
                ivList.get(i).visibility = View.VISIBLE
                valideIv.add(ivList.get(i))
            }
        }
        for (i in 0..item.mediaContent?.lastIndex!!) {
            Glide.with(context).load(item.mediaContent?.get(i)).into(valideIv.get(i))
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: MineDataBean, position: Int) {
        super.onClick(helper, view, data, position)
        itemClick(data.id, itemViewType)
    }

    override fun onChildClick(
        helper: BaseViewHolder,
        view: View,
        data: MineDataBean,
        position: Int
    ) {
        super.onChildClick(helper, view, data, position)
        when (view.id) {
            R.id.iv1,
            R.id.iv2,
            R.id.iv3,
            R.id.iv4,
            R.id.iv5,
            R.id.iv6,
            R.id.iv7,
            R.id.iv8,
            R.id.iv9 -> {
                imageClick(position, data.mediaContent)
            }
            R.id.tv_share -> {
                itemShare(data.id, itemViewType)
            }
            R.id.tv_praise -> {
                itemFavorite(data.id, itemViewType)
            }


        }
    }

}