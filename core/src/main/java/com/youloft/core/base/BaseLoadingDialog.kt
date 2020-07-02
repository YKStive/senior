package com.youloft.core.base

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.youloft.core.R

/**
 * @author you
 * @create 2020/7/2
 * @desc
 */
class BaseLoadingDialog(val context: FragmentActivity) : BaseDialog(context) {

    private lateinit var mLoading: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_progress_loading)
        mLoading = findViewById<ImageView>(R.id.iv_loading)
        bindUi()
    }

    private fun bindUi() {
        Glide.with(context)
            .load(R.drawable.ic_progress_loading)
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