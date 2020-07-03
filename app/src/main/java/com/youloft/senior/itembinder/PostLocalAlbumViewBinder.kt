package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.UserManager
import kotlinx.android.synthetic.main.item_post_local_album.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 本地影集item
 */
open class PostLocalAlbumViewBinder(
    val onItemClick: () -> Unit,
    val onShare: (paths: List<String>) -> Unit

) :
    ItemViewBinder<Post, PostLocalAlbumViewBinder.ViewHolder>() {
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

            header.setAvatar(UserManager.instance.getUserId())
            header.setTitle(UserManager.instance.getNickName())
            header.setDesc(item.createTime)


            album.setData(item.mediaContent)

            //条目
            setOnClickListener {
                onItemClick()
            }

            //分享
            btn_share.setOnClickListener {
                onShare(item.mediaContent)
            }


        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }


}