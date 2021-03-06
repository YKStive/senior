package com.youloft.senior.widgt

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.core.app.DialogCompat
import androidx.fragment.app.FragmentActivity
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import com.youloft.senior.base.COUNT_CONTEXT_IMAGE
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.ui.gif.GifActivity
import com.youloft.senior.ui.gif.GifListActivity
import com.youloft.senior.ui.graphic.ContentPublishActivity
import com.youloft.senior.ui.movie.MovieTemplateActivity
import com.youloft.senior.utils.logD
import kotlinx.android.synthetic.main.dialog_operate.*

/**
 * @author you
 * @create 2020/6/24
 * @desc 入口dialog
 */
class OperateDialog(val context: FragmentActivity) : BaseDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_operate)

        bindUi()
    }

    private fun bindUi() {
        root.setOnClickListener { dismiss() }
        container.setOnClickListener { "--".logD() }

        btn_image_text.setOnClickListener {
            ContentPublishActivity.start(context)
            dismiss()
        }

        btn_album.setOnClickListener {
            MovieTemplateActivity.start(context)
        }

        btn_gif.setOnClickListener {
            GifListActivity.start(context)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val window = window ?: return
        val attributes = window.attributes
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.dimAmount = 0.5f
    }
}