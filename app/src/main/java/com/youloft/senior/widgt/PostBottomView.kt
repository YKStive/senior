package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_COMMENT
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_PRAISE
import com.youloft.senior.itembinder.BaseRemotePostViewHolder.Companion.TYPE_SHARE
import kotlinx.android.synthetic.main.item_post_bottom_share.view.*

class PostBottomView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    init {
        LayoutInflater.from(App.instance()).inflate(R.layout.item_post_bottom_share, this)

    }


    fun setPost(post: Post, onItemClick: (type: Int, post: Post) -> Unit) {
        tv_comment.text = if (post.commented == 0) "评论" else post.commented.toString()
        tv_praise.text = if (post.praised == 0) null else post.praised.toString()
        tv_praise.isSelected = post.isPraised
        tv_share.setOnClickListener {
            onItemClick.invoke(TYPE_SHARE, post)
        }
        tv_comment.setOnClickListener {
            onItemClick.invoke(TYPE_COMMENT, post)
        }
        tv_praise.setOnClickListener {
            onItemClick.invoke(TYPE_PRAISE, post)
        }

    }


}