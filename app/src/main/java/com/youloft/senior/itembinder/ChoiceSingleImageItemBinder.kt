package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.coolktx.dp2px
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.widgt.ItemViewHolder
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.item_choice_image.view.*

class ChoiceSingleImageItemBinder(
    private val mItems: MutableList<ImageRes>
) : ItemViewBinder<ImageRes, RecyclerView.ViewHolder>() {
    var lastSelectedPosition: Int = -1

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val screenWidth = UiUtil.getScreenWidth(App.instance())
        val width = (screenWidth - 4.dp2px) / 3
        val inflate = inflater.inflate(R.layout.item_choice_image, parent, false)
        inflate.findViewById<ImageView>(R.id.iv_clover).apply {
            layoutParams.width = width
            layoutParams.height = width
        }
        return ItemViewHolder(inflate)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ImageRes) {

        holder.itemView.run {
            if (item.isSelected) {
                iv_check.setImageResource(R.drawable.ic_image_checked)
            } else {
                iv_check.setImageResource(R.drawable.ic_image_uncheck)
            }
            setOnClickListener {
                val new = holder.layoutPosition
                if (lastSelectedPosition == -1) {
                    mItems[new].isSelected = true
                    adapter.notifyItemChanged(new)
                    lastSelectedPosition = new

                } else if (lastSelectedPosition == new) {
                    mItems[new].isSelected = !mItems[new].isSelected
                    adapter.notifyItemChanged(new)
                    if (mItems[new].isSelected) {
                        lastSelectedPosition = new
                    } else {
                        lastSelectedPosition = -1
                    }
                } else {
                    mItems[lastSelectedPosition].isSelected = false
                    mItems[new].isSelected = true
                    adapter.notifyItemChanged(lastSelectedPosition)
                    adapter.notifyItemChanged(new)
                    lastSelectedPosition = new
                }
            }

            Glide.with(context)
                .load(item.path)
                .into(iv_clover)
        }
    }

}