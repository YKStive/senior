package com.youloft.senior.itembinder

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
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.ImageLoader
import com.youloft.senior.utils.isByUser
import com.youloft.senior.widgt.PostItemMultiImage
import kotlinx.android.synthetic.main.item_post_gif_image.view.*
import okhttp3.internal.waitMillis

/**
 * @author you
 * @create 2020/6/22
 * @desc gif  item
 */
open class PostGifViewBinder(
    private val onOperate: (type: Int, post: Post) -> Unit
) :
    ItemViewBinder<Post, PostGifViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        return ViewHolder(
            parent, onOperate
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {

        holder.bind(item)
    }


    class ViewHolder(
        parent: ViewGroup,
        onOperate: (type: Int, post: Post) -> Unit
    ) :
        BaseRemotePostViewHolder(parent, onOperate) {


        override fun addContentView(): View {
            return ImageView(App.instance()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    App.instance().resources.getDimension(R.dimen.app_post_gif_width).toInt(),
                    App.instance().resources.getDimension(R.dimen.app_post_gif_height).toInt()

                )
                scaleType = ImageView.ScaleType.CENTER_CROP
                id = R.id.iv_post_item_content
            }


        }

        override fun setContent(post: Post) {
            val content: ImageView = itemView.findViewById(R.id.iv_post_item_content)
            ImageLoader.loadImage(content, post.mediaContent[0], content)
        }


    }


}