package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import kotlinx.android.synthetic.main.item_post_video.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 视频item
 */
open class PostVideoViewBinder(
    val onInvite: (btnInvite: Button) -> Unit

) :
    ItemViewBinder<Post, PostVideoViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_post_video, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {

        holder.itemView.run {
            tv_content.text = item.textContent
            setPlayer(item, player)
        }
    }

    private fun setPlayer(item: Post, player: ImageView) {
        if (item.width < item.height) {
            player.apply {
                layoutParams.width =
                    App.instance().resources.getDimension(R.dimen.app_post_item_video_vertical_width)
                        .toInt()
                layoutParams.height =
                    App.instance().resources.getDimension(R.dimen.app_post_item_video_vertical_height)
                        .toInt()
            }
        } else {
            player.apply {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height =
                    App.instance().resources.getDimension(R.dimen.app_post_item_video_vertical_width)
                        .toInt()
            }
        }

        Glide.with(player.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .centerCrop()
                    .error(R.drawable.ic_placeholder_error)
                    .placeholder(R.drawable.ic_placeholder_error)
            )
            .load(item.mediaContent[0])
            .into(player)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }


}