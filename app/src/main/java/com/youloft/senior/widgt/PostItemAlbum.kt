package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.youloft.coolktx.dp2px
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.util.UiUtil

/**
 * @author you
 * @create 2020/6/23
 * @desc 贴子--相册content
 */
class PostItemAlbum(context: Context, attr: AttributeSet?) : AutoScrollRecycleView(context, attr) {

    companion object {
        private var mWith: Int =
            App.instance().resources.getDimension(R.dimen.app_post_album_width).toInt()
        private var mHeight =
            App.instance().resources.getDimension(R.dimen.app_post_album_height).toInt()
    }

    init {
        if (attr == null) {
            val params = LayoutParams(mWith, mHeight)
            layoutParams = params
        }
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setWithAndHeight(with: Int, height: Int) {
        mWith = with.dp2px
        mHeight = height.dp2px
        val params = LayoutParams(mWith, mHeight)
        layoutParams = params
    }

    fun setData(path: List<String>) {
        val albumAdapter = AlbumAdapter(path)
        adapter = albumAdapter
    }


    internal class AlbumAdapter(private val items: List<String>) :
        Adapter<AlbumAdapter.AlbumHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
            val screenWidth = UiUtil.getScreenWidth(App.instance())
            val width =
                screenWidth - App.instance().resources.getDimension(R.dimen.app_post_padding)
                    .toInt()
            val layoutParams = LayoutParams(width, width)
            val imageView = ImageView(parent.context)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.layoutParams = layoutParams
            return AlbumHolder(imageView)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
            Glide.with(holder.itemView).load(items[position])
                .into(holder.itemView as ImageView)

        }

        class AlbumHolder(view: View) : ViewHolder(view)

    }
}