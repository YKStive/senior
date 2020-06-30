package com.youloft.senior.itembinder

import android.graphics.Color
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
import com.youloft.senior.bean.Comment
import com.youloft.senior.bean.GifBean
import com.youloft.senior.bean.Praise
import com.youloft.senior.utils.DateUtil
import com.youloft.senior.utils.ImageLoader
import kotlinx.android.synthetic.main.item_received_comment.view.*


/**
 * @author you
 * @create 2020/6/22
 * @desc 收到的评论
 */
open class ReceivedPraiseForPostViewBinder(
    val onItemClick: (id: String, type: Int) -> Unit
) :
    ItemViewBinder<Praise, ReceivedPraiseForPostViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: View =
            inflater.inflate(R.layout.item_received_praise_for_post, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Praise
    ) {

        holder.itemView.run {
            header.setAvatar(item.avatar)
            header.setTitle(item.nickname)
            val str2Date = DateUtil.str2Date(item.createTime, DateUtil.FORMAT_FULL_SN)
            val date2Str = DateUtil.date2Str(str2Date, "mm-yyyy HH:mm")
            header.setDesc(date2Str)

            ImageLoader.loadImage(this, item.commentUserAvatar, iv_avatar)
            tv_nick_name.text = item.commentUserNickname
            if (item.textContent.isNotEmpty()) {
                tv_content.setTextColor(Color.parseColor("#F7612C"))
                tv_content.text = "点击查看详情 >"
            } else {
                tv_content.setTextColor(Color.parseColor("#555555"))
                tv_content.text = item.textContent
            }

            comment_container.setOnClickListener {
                onItemClick.invoke(item.postId, item.postType)
            }

        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        init {

        }

    }


}