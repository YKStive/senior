package com.youloft.senior.itembinder

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
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.widgt.PostItemMultiImage
import kotlinx.android.synthetic.main.item_post_multi_image.view.*
import kotlinx.android.synthetic.main.item_post_video.view.*
import kotlinx.android.synthetic.main.item_post_video.view.tv_content
import okhttp3.internal.waitMillis

/**
 * @author you
 * @create 2020/6/22
 * @desc 多图item
 */
open class PostMultiImageViewBinder(
    private val pool: RecyclerView.RecycledViewPool? = null,
    private val onOperate: (type: Int, post: Post) -> Unit,
    private val onImageClick: (position: Int, data: List<String>) -> Unit
) :
    ItemViewBinder<Post, PostMultiImageViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        return ViewHolder(
            pool = pool, parent = parent, onOperate = onOperate, onImageClick = onImageClick
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {

        holder.bind(post = item)
    }


    class ViewHolder(
        parent: ViewGroup,
        private val pool: RecyclerView.RecycledViewPool? = null,
        onOperate: (type: Int, post: Post) -> Unit,
        private val onImageClick: (position: Int, data: List<String>) -> Unit
    ) :
        BaseRemotePostViewHolder(parent, onOperate) {


        override fun addContentView(): View {
            return PostItemMultiImage(App.instance(), null).apply {
                id = R.id.iv_post_item_content
            }
        }

        override fun setContent(post: Post) {
            val content = itemView.findViewById<PostItemMultiImage>(R.id.iv_post_item_content)
            content.setData(pool, post.mediaContent) {
                onImageClick(it, post.mediaContent)
            }
        }


    }

}