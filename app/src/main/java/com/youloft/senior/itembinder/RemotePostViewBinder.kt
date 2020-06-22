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
import kotlinx.android.synthetic.main.home_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_post_remote.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 信息流多图item
 */
open class RemotePostViewBinder(
    private val goPersonPage: (userId: String) -> Unit,
    val onItemClick: (postId: String, openComment: Boolean) -> Unit,
    val onShare: (postId: String) -> Unit,
    val onPraise: (postId: String) -> Unit
) :
    ItemViewBinder<Post, RemotePostViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: ConstraintLayout =
            inflater.inflate(R.layout.item_post_remote, parent, false) as ConstraintLayout
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {

        holder.addHeader(item, goPersonPage)
        holder.itemView.run {

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
            tv_praise.text = item.praised.toString()
            tv_praise.setOnClickListener {
                onPraise(item.id)
            }

        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        /**
         * 是否是用户自己的帖子，展示不同的header
         * @param post Post 帖子
         */
        fun addHeader(post: Post, goPersonPage: (userId: String) -> Unit) {
            val header: View = if (post.userId.isByUser()) {
                LayoutInflater.from(App.instance())
                    .inflate(R.layout.item_post_header_main, itemView as ViewGroup, false)
            } else {
                LayoutInflater.from(App.instance())
                    .inflate(R.layout.item_post_header_other, itemView as ViewGroup, false)
            }
            itemView.fl_header_container.addView(header)

            val ivAvatar = header.findViewById<ImageView>(R.id.iv_avatar)
            val tvNickname = header.findViewById<TextView>(R.id.tv_name)
            Glide.with(itemView).load(post.avatar).error(R.drawable.ic_placeholder_error)
                .into(ivAvatar)
            tvNickname.text = post.nickname

            ivAvatar.setOnClickListener {
                goPersonPage(post.userId)
            }

            tvNickname.setOnClickListener {
                goPersonPage(post.userId)
            }


            header.findViewById<TextView>(R.id.tv_view_amount).text =
                String.format(
                    App.instance().resources.getString(R.string.viewed_amount),
                    post.viewed
                )
        }
    }


}