package com.youloft.senior.itembinder

import android.content.Context
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
import com.youloft.senior.bean.PostType
import com.youloft.senior.utils.ImageLoader
import com.youloft.coolktx.dp2px
import com.youloft.senior.utils.isByUser
import com.youloft.senior.widgt.PostItemAlbum
import com.youloft.senior.widgt.PostItemMultiImage
import com.youloft.senior.widgt.VideoPlay
import kotlinx.android.synthetic.main.item_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_post_remote.view.*

/**
 * @author you
 * @create 2020/6/22
 * @desc 远程贴子，处理多种类型
 */
open class RemotePostViewBinder(
    private val goPersonPage: (userId: String) -> Unit,
    val onItemClick: (postId: String, openComment: Boolean) -> Unit,
    val onShare: (postId: String) -> Unit,
    val onPraise: (postId: String) -> Unit
) :
    ItemViewBinder<Post, RemotePostViewBinder.RemoteViewHolder>() {
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
            tv_praise.apply {
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
                        VideoPlay(context).apply { setVideo(post.mediaContent[0]) }
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
            imageView.scaleType = ImageView.ScaleType.FIT_XY
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
            val header: View = if (post.userId.isByUser()) {
                LayoutInflater.from(App.instance())
                    .inflate(R.layout.item_post_header_main, itemView as ViewGroup, false)
            } else {
                LayoutInflater.from(App.instance())
                    .inflate(R.layout.item_post_header_other, itemView as ViewGroup, false)
            }
            headerContainer.addView(header)

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