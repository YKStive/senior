package com.youloft.senior.widgt

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.utils.dp2px
import kotlinx.android.synthetic.main.item_post_remote_content_multi_image.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc  贴子--多图content
 */
class PostItemMultiImage(context: Context, attributeSet: AttributeSet?) :
    RecyclerView(context, attributeSet) {

    init {

        layoutManager = GridLayoutManager(context, 3)
        addItemDecoration(
            RecycleViewDivider(
                context,
                LinearLayoutManager.VERTICAL,
                5.dp2px,
                Color.parseColor("#ffffff")
            )
        )


    }


    fun setData(path: List<String>) {
        val multiImageAdapter = MultiImageAdapter(path)
        adapter = multiImageAdapter
    }

    internal class MultiImageAdapter(private val items: List<String>) :
        Adapter<MultiImageAdapter.MultiImageHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiImageHolder {
            val imageView = ImageView(parent.context)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            val layoutParams = LayoutParams(104.dp2px, 104.dp2px)
            imageView.layoutParams = layoutParams
            return MultiImageHolder(imageView)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: MultiImageHolder, position: Int) {
            Glide.with(holder.itemView).load(items[position]).into(holder.itemView as ImageView)
        }

        class MultiImageHolder(view: View) : ViewHolder(view) {

        }

    }


}