package com.youloft.senior.itembinder

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.bean.Praise
import com.youloft.senior.utils.DateUtil
import com.youloft.senior.utils.ImageLoader
import kotlinx.android.synthetic.main.item_received_comment.view.*
import kotlinx.android.synthetic.main.item_received_comment.view.comment_container
import kotlinx.android.synthetic.main.item_received_comment.view.header
import kotlinx.android.synthetic.main.item_received_comment.view.iv_avatar
import kotlinx.android.synthetic.main.item_received_comment.view.tv_content
import kotlinx.android.synthetic.main.item_received_comment.view.tv_nick_name
import kotlinx.android.synthetic.main.item_received_praise_for_comment.view.*


/**
 * @author you
 * @create 2020/6/22
 * @desc 收到的赞for评论
 */
open class ReceivedPraiseForCommentViewBinder(
    val onItemClick: (id: String, type: Int) -> Unit
) :
    ItemViewBinder<Praise, ReceivedPraiseForCommentViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: View =
            inflater.inflate(R.layout.item_received_praise_for_comment, parent, false)
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

            //评论者昵称和内容
            val spannableString = SpannableString("${item.commentUserNickname}: ${item.content}")
            val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#F7612C"))
            spannableString.setSpan(
                foregroundColorSpan,
                0,
                item.nickname.length + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tv_comment_praise.text = spannableString



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