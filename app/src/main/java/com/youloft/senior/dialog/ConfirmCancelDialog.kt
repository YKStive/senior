package com.youloft.senior.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import com.youloft.senior.cash.BindPhoneActivity
import kotlinx.android.synthetic.main.phone_verify_dialog_layout.*

/**
 * @author you
 * @create 2020/7/3
 * @desc
 */

class ConfirmCancelDialog(
    activity: FragmentActivity,
    val content: String? = null,
    private val onConfirm: ((view: Dialog) -> Unit)? = null,
    private val onCancel: (() -> Unit)? = null
) : BaseDialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setContentView(R.layout.phone_verify_dialog_layout)
        tv_content.text = content
        cancel.setOnClickListener {
            onCancel?.invoke()
            dismiss()
        }
        bind.setOnClickListener {
            onConfirm?.invoke(this)
        }
    }

    fun setContent(str: String) {
        tv_content.text = str
    }

    fun setConfirmText(str: String) {
        bind.text = str
    }

    fun setCancelText(str: String) {
        cancel.text = str
    }
}