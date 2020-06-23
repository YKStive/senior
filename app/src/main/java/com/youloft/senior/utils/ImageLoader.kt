package com.youloft.senior.utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.youloft.senior.R

/**
 * @author you
 * @create 2020/6/23
 * @desc
 */

class ImageLoader {
    companion object {
        fun loadImage(activity: Activity, path: String, target: ImageView) {
            Glide.with(activity).load(path)
                .placeholder(R.drawable.ic_placeholder_error)
                .error(R.drawable.ic_placeholder_error)
                .into(target)
        }

        fun loadImage(view: View, path: String, target: ImageView) {
            Glide.with(view).load(path)
                .placeholder(R.drawable.ic_placeholder_error)
                .error(R.drawable.ic_placeholder_error)
                .into(target)
        }

        fun loadImage(fragment: Fragment, path: String, target: ImageView) {
            Glide.with(fragment).load(path)
                .placeholder(R.drawable.ic_placeholder_error)
                .error(R.drawable.ic_placeholder_error)
                .into(target)
        }
    }
}
