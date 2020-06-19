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

class ChoiceImageItemBinder(
    var isSingle: Boolean = true,
    var onItemClick: (position: Int, result: ArrayList<ImageRes>) -> Unit
) : ItemViewBinder<ImageRes, RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ItemViewHolder(inflater.inflate(R.layout.item_choice_image, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ImageRes) {
        holder.itemView.run {
            setOnClickListener {
                if (isSingle) {
                    onItemClick.invoke(holder.adapterPosition, arrayListOf(item))
                } else {
                    cb_checked.isChecked = !cb_checked.isChecked
                    item.isSelected = cb_checked.isChecked
                }
            }

            Glide.with(context)
                .load(item.path)
                .into(iv_clover)
        }
    }

}