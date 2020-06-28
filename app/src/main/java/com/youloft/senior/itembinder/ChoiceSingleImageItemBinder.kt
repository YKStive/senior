package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.widgt.ItemViewHolder
import kotlinx.android.synthetic.main.item_choice_image.view.*

class ChoiceSingleImageItemBinder(
    val onItemClick: (oldPosition: Int,newPosition: Int) -> Unit
) : ItemViewBinder<ImageRes, RecyclerView.ViewHolder>() {
    var lastSelectedPosition: Int = -1

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ItemViewHolder(inflater.inflate(R.layout.item_choice_image, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ImageRes) {

        holder.itemView.run {
            if (item.isSelected) {
                iv_check.setImageResource(R.drawable.ic_image_checked)
            } else {
                iv_check.setImageResource(R.drawable.ic_image_uncheck)
            }
            setOnClickListener {
                onItemClick(lastSelectedPosition,holder.layoutPosition)
            }

            Glide.with(context)
                .load(item.path)
                .into(iv_clover)
        }
    }

}