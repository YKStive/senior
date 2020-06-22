package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.isByUser
import kotlinx.android.synthetic.main.home_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_multi_image.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 信息流多图item
 */
open class MultiImageViewBinder(
    val goPersonPage: (userId: String) -> Unit,
    val onItemClick: (postId: String, openComment: Boolean?) -> Unit,
    val onShare: (postId: String) -> Unit,
    val onPraise: (postId: String) -> Unit
) :
    ItemViewBinder<Post, MultiImageViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: ConstraintLayout = inflater.inflate(R.layout.item_multi_image, parent, false) as ConstraintLayout

        root.run {
        }
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {
        if (item.userId.isByUser()) {

        }


        holder.itemView.run {
            Glide.with(this).load(item.avatar).into(iv_avatar)
            tv_name.text = item.nickname
            tv_view_amount.text =
                String.format(context.resources.getString(R.string.viewed_amount), item.viewed)
            tv_content.text = item.textContent

            //todo 多图


            //条目
            setOnClickListener {
                onItemClick(item.id, false)
            }

            //分享
            tv_share.setOnClickListener {
                onShare(item.id)
            }

            //评论
            tv_comment.setOnClickListener {
                onItemClick(item.id, true)
            }

            //点赞
            tv_praise.setOnClickListener {
                onPraise(item.id)
            }

        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)


}