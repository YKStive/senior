package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.youloft.senior.R
import com.youloft.senior.utils.ImageLoader

/**
 * @author you
 * @create 2020/6/23
 * @desc 头部
 */
class PostHeaderView(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    private var isMy: Boolean = false
    private var avatar: String? = null
    private var title: String? = null
    private var desc: String? = null
    private var ivAvatar: ImageView
    private var tvTitle: TextView
    private var tvDesc: TextView

    init {
        attributeSet?.apply {
            val types = context.obtainStyledAttributes(attributeSet, R.styleable.PostHeaderView)
            isMy = types.getBoolean(R.styleable.PostHeaderView_hv_is_main, false)
            avatar = types.getString(R.styleable.PostHeaderView_hv_avatar)
            title = types.getString(R.styleable.PostHeaderView_hv_title)
            desc = types.getString(R.styleable.PostHeaderView_hv_desc)
            types.recycle()
        }
        val root = if (isMy) {
            LayoutInflater.from(context).inflate(R.layout.item_post_header_main, this)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_post_header_other, this)

        }
        ivAvatar = root.findViewById(R.id.iv_avatar)
        tvTitle = root.findViewById(R.id.tv_name)
        tvDesc = root.findViewById(R.id.tv_view_amount)


        setAvatar(avatar)
        setTitle(title)
        setDesc(desc)
    }

    fun setAvatar(avatar: String?) {
        avatar?.apply {
            ImageLoader.loadImage(this@PostHeaderView, this, ivAvatar)
        }
    }

    fun setTitle(title: String?) {
        title?.apply {
            tvTitle.text = title
        }
    }

    fun setDesc(desc: String?) {
        desc?.apply {
            tvDesc.text = title
        }
    }
}