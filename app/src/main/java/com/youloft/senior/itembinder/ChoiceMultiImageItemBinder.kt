package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.toast
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.widgt.ItemViewHolder
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.item_choice_image.view.*

class ChoiceMultiImageItemBinder(
    private val countLimit: Int,
    private val mItems: MutableList<ImageRes>
) : ItemViewBinder<ImageRes, RecyclerView.ViewHolder>() {


    val selectedCount: MutableLiveData<Int> = MutableLiveData()

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
                when (item.type) {
                    ImageRes.TYPE_VIDEO -> {
                        val selectedImageCount = getSelectedImageCount()
                        if (selectedImageCount > 0) {
                            context.toast("不能同时选择视频和图片")
                            return@setOnClickListener
                        }
                        limit(getSelectedVideoCount(), 1, item, "只能选择一个视频", holder)
                    }

                    ImageRes.TYPE_IMAGE -> {
                        val selectedVideoCount = getSelectedVideoCount()
                        if (selectedVideoCount > 0) {
                            context.toast("不能同时选择视频和图片")
                            return@setOnClickListener
                        }
                        limit(
                            getSelectedImageCount(),
                            countLimit,
                            item,
                            "照片最多能选${countLimit}长",
                            holder
                        )
                    }
                }
            }

            Glide.with(context)
                .load(item.path)
                .into(iv_clover)
        }
    }

    private fun View.limit(
        count: Int,
        limit: Int,
        item: ImageRes,
        toastStr: String,
        holder: RecyclerView.ViewHolder
    ) {
        if (item.isSelected || count < limit) {
            item.isSelected = !item.isSelected
            adapter.notifyItemChanged(holder.layoutPosition)
            selectedCount.value = count+1
        } else {
            context.toast(toastStr)
        }
    }

    private fun getSelectedImageCount(): Int {
        return mItems.filter {
            it.type == ImageRes.TYPE_IMAGE && it.isSelected
        }.size
    }

    private fun getSelectedVideoCount(): Int {
        return mItems.filter {
            it.type == ImageRes.TYPE_VIDEO && it.isSelected
        }.size
    }

}