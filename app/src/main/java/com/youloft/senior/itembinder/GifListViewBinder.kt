package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.GifBean
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.ImageLoader
import com.youloft.senior.utils.isByUser
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.item_gif_list.view.*
import kotlinx.android.synthetic.main.item_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_post_header_other.view.*
import kotlinx.android.synthetic.main.item_post_remote.view.*
import kotlinx.android.synthetic.main.item_post_punch.view.*
import kotlin.random.Random

/**
 * @author you
 * @create 2020/6/22
 * @desc gif列表
 */
open class GifListViewBinder(
    val onMake: (tempPath: String) -> Unit,
    val onUse: (resPath: String) -> Unit

) :
    ItemViewBinder<GifBean, GifListViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: View =
            inflater.inflate(R.layout.item_gif_list, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: GifBean
    ) {

        holder.itemView.run {
            ImageLoader.loadImage(holder.itemView, item.material, iv_gif)
            tv_user.setOnClickListener {
                onUse(item.material)
            }
            tv_make.setOnClickListener {
                onMake(item.thumbnail)
            }
        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        init {
        }

    }


}