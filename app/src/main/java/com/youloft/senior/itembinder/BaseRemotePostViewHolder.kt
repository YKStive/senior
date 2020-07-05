package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.isByUser
import kotlinx.android.synthetic.main.item_post_remote.view.*

abstract class BaseRemotePostViewHolder(
    parent: ViewGroup,
    protected val onOperate: (type: Int, post: Post) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(App.instance()).inflate(R.layout.item_post_remote, parent, false)
) {

    abstract fun addContentView(): View?


    init {
        itemView.fl_content_container.addView(addContentView())
    }


    fun bind(post: Post) {
        itemView.run {
            setOnClickListener {
                onOperate.invoke(TYPE_ITEM, post)
            }

            header.apply {
                setSelfLayout(post.userId.isByUser())
                setAvatar(post.avatar)
                setTitle(post.nickname)
                setDesc("${post.viewed}次浏览")
                onAvatarClick { onOperate.invoke(TYPE_HEADER, post) }
            }

            tv_content.apply {
                visibility = if (post.textContent.isBlank()) View.GONE else View.VISIBLE
                text = post.textContent
            }


            setContent(post)


            bottom_view.setPost(post.apply { position = layoutPosition }, onItemClick = onOperate)
        }

    }


    abstract fun setContent(post: Post)

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
        const val TYPE_SHARE = 2
        const val TYPE_COMMENT = 3
        const val TYPE_PRAISE = 4
        const val TYPE_CONTENT = 5

    }


}