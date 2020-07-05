package com.youloft.senior.itembinder

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.drakeet.multitype.ItemViewBinder
import com.umeng.socialize.media.Base
import com.youloft.coolktx.dp2px
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import kotlinx.android.synthetic.main.item_post_video.view.*
import okhttp3.internal.waitMillis

/**
 * @author you
 * @create 2020/6/22
 * @desc 视频item
 */
open class PostVideoViewBinder(
    private val onOperate: (type: Int, post: Post) -> Unit

) :
    ItemViewBinder<Post, BaseRemotePostViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        return ViewHolder(
            parent, onOperate
        )
    }

    override fun onBindViewHolder(
        holder: BaseRemotePostViewHolder, item: Post
    ) {
        holder.bind(item)
    }


    class ViewHolder(
        parent: ViewGroup,
        onOperate: (type: Int, post: Post) -> Unit
    ) :
        BaseRemotePostViewHolder(parent, onOperate) {


        override fun addContentView(): View {
            val container = FrameLayout(App.instance())
            container.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val thumbView = ImageView(App.instance())
            thumbView.id = R.id.iv_post_item_content
            thumbView.apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            val playView = ImageView(App.instance())
            playView.apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
                setImageResource(R.drawable.ic_video_play)
            }
            container.addView(thumbView)
            container.addView(playView)
            return container
        }

        override fun setContent(post: Post) {
            val player = itemView.findViewById<ImageView>(R.id.iv_post_item_content)
            setPlayer(post, player)
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

    }


}