package com.youloft.senior.itembinder

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.drakeet.multitype.ItemViewBinder
import com.youloft.coolktx.dp2px
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.PostType
import com.youloft.senior.utils.ImageLoader
import com.youloft.senior.utils.isByUser
import com.youloft.senior.widgt.PostHeaderView
import com.youloft.senior.widgt.PostItemAlbum
import com.youloft.senior.widgt.PostItemMultiImage
import kotlinx.android.synthetic.main.item_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_post_remote.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 远程贴子，处理多种类型
 */
open class PostRemoteViewBinder(
    private val goPersonPage: (userId: String) -> Unit,
    val onItemClick: (post: Post, openComment: Boolean?) -> Unit,
    val onShare: (postId: String) -> Unit,
    val onPraise: (postId: String) -> Unit

) : ItemViewBinder<Post, PostRemoteViewBinder.RemoteViewHolder>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RemoteViewHolder {
        val root: ConstraintLayout =
            inflater.inflate(R.layout.item_post_remote, parent, false) as ConstraintLayout
        return RemoteViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: RemoteViewHolder, item: Post
    ) {

        holder.addHeader(item, goPersonPage)
        holder.addContent(item, holder.itemView.context)
        holder.itemView.run {
            tv_content.text = item.textContent
            //条目
            setOnClickListener {
                onItemClick(item, null)
            }

            //分享
            tv_share.setOnClickListener {
                onShare(item.id)
            }

            //评论
            tv_comment.text = if (item.commented == 0) "评论" else item.commented.toString()
            tv_comment.setOnClickListener {
                onItemClick(item, true)
            }

            //点赞
            tv_praise.apply {
                isSelected = item.isPraised
                text = item.praised.toString()
                setOnClickListener {
                    isSelected = !isSelected
                    text = if (isSelected) {
                        (text.toString().toInt() + 1).toString()
                    } else {
                        (text.toString().toInt() - 1).toString()
                    }
                    onPraise(item.id)
                }
            }

        }
    }

    class RemoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val contentContainer by lazy {
            itemView.fl_content_container
        }

        private val headerContainer by lazy {
            itemView.fl_header_container
        }

        /**
         * 根据不同类型，添加不同的content
         * @param post Post 贴子
         */
        fun addContent(post: Post, context: Context) {
            contentContainer.removeAllViews()
            when (post.postType) {
                //图文
                PostType.IMAGE_TEXT -> {
                    if (post.mediaContent.size > 1) {
                        val multiImageView = PostItemMultiImage(App.instance(), null)
                        multiImageView.setData(post.mediaContent)
                        contentContainer.addView(multiImageView)
                    } else if (post.mediaContent.size == 1) {
                        setSingleImage(post)
                    }

                }

                //gif
                PostType.GIF -> {
                    setSingleImage(post)
                }

                //视频
                PostType.VIDEO -> {
                    if (post.mediaContent.isNotEmpty()) {
                        val container = FrameLayout(context)
                        container.apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                        val thumbView = ImageView(context)
                        thumbView.apply {
                            layoutParams = if (post.width < post.height) ViewGroup.LayoutParams(
                                178.dp2px,
                                315.dp2px
                            ) else ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                178.dp2px
                            )
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val playView = ImageView(context)
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
                        contentContainer.addView(container)
                        Glide.with(thumbView.context)
                            .setDefaultRequestOptions(
                                RequestOptions()
                                    .frame(1000000)
                                    .centerCrop()
                                    .error(R.drawable.ic_placeholder_error)
                                    .placeholder(R.drawable.ic_placeholder_error)
                            )
                            .load(post.mediaContent[0])
                            .into(thumbView)

                    }

                }

                //影集
                PostType.ALBUM -> {
                    val postItemAlbum = PostItemAlbum(App.instance(), null)
                    postItemAlbum.setData(post.mediaContent)
                    contentContainer.addView(postItemAlbum)
                }

            }
        }

        /**
         * 设置单张图片
         * @param post Post
         */
        private fun setSingleImage(post: Post) {
            val imageView = ImageView(App.instance())
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val layoutParams = ViewGroup.LayoutParams(
                App.instance().resources.getDimension(R.dimen.app_post_gif_width).toInt(),
                App.instance().resources.getDimension(R.dimen.app_post_gif_height).toInt()
            )
            imageView.layoutParams = layoutParams
            ImageLoader.loadImage(contentContainer, post.mediaContent[0], imageView)
            contentContainer.addView(imageView)
        }


        /**
         * 是否是用户自己的帖子，展示不同的header
         * @param post Post 帖子
         */
        fun addHeader(post: Post, goPersonPage: (userId: String) -> Unit) {
            headerContainer.removeAllViews()
            headerContainer.addView(PostHeaderView(App.instance(), post.userId.isByUser()).apply {
                setAvatar(post.avater)
                setTitle(post.nickname)
                setDesc("${post.viewed}次浏览")
                onAvatarClick { goPersonPage(post.userId) }
            })
        }
    }


}