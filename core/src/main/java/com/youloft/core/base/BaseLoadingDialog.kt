package com.youloft.core.base

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.youloft.core.R
import java.security.MessageDigest
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation


/**
 * @author you
 * @create 2020/7/2
 * @desc
 */
class BaseLoadingDialog(val context: FragmentActivity) : BaseDialog(context) {

    private lateinit var mLoading: ImageView
    private val circleCrop: Transformation<Bitmap> by lazy {
        object : BitmapTransformation() {
            override fun updateDiskCacheKey(messageDigest: MessageDigest) {
            }

            override fun transform(
                pool: BitmapPool,
                toTransform: Bitmap,
                outWidth: Int,
                outHeight: Int
            ): Bitmap {
                return toTransform
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_progress_loading)
        mLoading = findViewById(R.id.iv_loading)
        bindUi()
    }

    private fun bindUi() {
        Glide.with(context)
            .load(R.drawable.ic_progress_loading)
            .optionalTransform(WebpDrawable::class.java, WebpDrawableTransformation(circleCrop))
            .into(mLoading)
    }

    override fun onStart() {
        super.onStart()
        val window = window ?: return
        val attributes = window.attributes
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER
        attributes.dimAmount = 0.5f
    }
}