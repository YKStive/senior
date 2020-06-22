package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.isByUser
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.home_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_post_local_album.view.*
import kotlinx.android.synthetic.main.item_post_remote.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 本地影集item
 */
open class LocalAlbumViewBinder(
    val onItemClick: () -> Unit,
    val onShare: () -> Unit

) :
    ItemViewBinder<Post, LocalAlbumViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: ConstraintLayout =
            inflater.inflate(R.layout.item_post_local_album, parent, false) as ConstraintLayout
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {

        holder.itemView.run {


            //条目
            setOnClickListener {
                onItemClick()
            }

            //分享
            btn_share.setOnClickListener {
                onShare()
            }


        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }


}