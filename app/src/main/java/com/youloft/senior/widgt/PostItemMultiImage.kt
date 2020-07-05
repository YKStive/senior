package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youloft.coolktx.dp2px
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.util.UiUtil

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
            GridSpaceItemDecoration(3, 5.dp2px, 5.dp2px)
        )
    }


    fun setData(pool: RecycledViewPool?=null,path: List<String>, onImageClick: (position: Int) -> Unit) {
        setRecycledViewPool(pool)
        val multiImageAdapter = MultiImageAdapter(path, onImageClick)
        adapter = multiImageAdapter
    }

    internal class OnClick(private val conImageClick: (position: Int) -> Unit) : OnClickListener {
        var mPosition = 0
        override fun onClick(v: View) {
            conImageClick.invoke(mPosition)
        }

    }


    internal class MultiImageAdapter(
        private val items: List<String>,
        private val onImageClick: (position: Int) -> Unit
    ) :
        Adapter<MultiImageAdapter.MultiImageHolder>() {

        private val onClick: OnClick = OnClick { onImageClick }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiImageHolder {
            val imageView = ImageView(parent.context)
            val screenWidth = UiUtil.getScreenWidth(App.instance())
            val width = (screenWidth - 60.dp2px - 10.dp2px) / 3
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val layoutParams = LayoutParams(
                width,
                width
            )
            imageView.layoutParams = layoutParams

            return MultiImageHolder(imageView)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: MultiImageHolder, position: Int) {
            val imageView = holder.itemView as ImageView
            imageView.setOnClickListener(onClick.apply {
                mPosition = holder.adapterPosition
            })
            Glide.with(holder.itemView).load(items[position])
                .into(imageView)
        }

        class MultiImageHolder(view: View) : ViewHolder(view) {

        }


    }


}