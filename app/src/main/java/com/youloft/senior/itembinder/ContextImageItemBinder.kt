package com.youloft.senior.itembinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.noober.background.view.BLImageView
import com.youloft.coolktx.dp2px
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.widgt.ItemViewHolder
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.item_choice_image.view.*
import kotlinx.android.synthetic.main.item_context_image.view.*

class ContextImageItemBinder(
    private val mItems: MutableList<ImageRes>,
    private val onItemClick: (isAddImage: Boolean, position: Int) -> Unit
) : ItemViewBinder<ImageRes, RecyclerView.ViewHolder>() {

    init {
        val imageRes = ImageRes("")
        imageRes.isAddIcon = true
        mItems.add(imageRes)
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val screenWidth = UiUtil.getScreenWidth(App.instance())
        val width = (screenWidth - 30.dp2px - 10.dp2px) / 3
        val inflate = inflater.inflate(R.layout.item_context_image, parent, false)
        inflate.findViewById<ImageView>(R.id.iv_image).apply {
            layoutParams.width = width
            layoutParams.height = width
        }
        return ItemViewHolder(inflate)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ImageRes) {

        holder.itemView.run {
            iv_image.setOnClickListener {
                onItemClick.invoke(item.isAddIcon, holder.layoutPosition)
            }

            iv_delete_image.setOnClickListener {
                if (!item.isAddIcon) {
                    mItems.removeAt(holder.layoutPosition)
                    adapter.notifyItemRemoved(holder.layoutPosition)
                }
            }
            iv_delete_image.visibility = if (!item.isAddIcon) View.VISIBLE else View.INVISIBLE
            if (item.isAddIcon) {
                val blImageView = iv_image as BLImageView
//                blImageView.setImageResource()
                blImageView.setBackgroundColor(Color.parseColor("#F6F7F8"))
            } else {
                Glide.with(context)
                    .load(item.path)
                    .into(iv_image)
            }
        }
    }

}