package com.youloft.senior.widgt

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import com.youloft.senior.base.App
import kotlinx.android.synthetic.main.dialog_privacy.*

/**
 * @author you
 * @create 2020/6/24
 * @desc 隐私协议
 */
class PrivacyDialog(val context: FragmentActivity, private val onAgree: () -> Unit) :
    BaseDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_privacy)

        bindUi()
    }

    private fun bindUi() {
        setCancelable(false)


        val protocol = App.instance().getString(R.string.user_protocol)
        val privacy = App.instance().getString(R.string.privacy)
        val desc = App.instance().getString(R.string.protocol_desc)
        val limit1 = arrayOf(20, 20 + protocol.length)
        val limit2 = arrayOf(21 + protocol.length, 21 + protocol.length + privacy.length)
        val foregroundColorSpan = ForegroundColorSpan(Color.RED)

        tv_protocol_desc.text =
            SpannableStringBuilder(String.format(desc, protocol, privacy)).apply {
                setSpan(
                    foregroundColorSpan,
                    limit1[0],
                    limit1[1],
                    SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
                )
                setSpan(
                    CharacterStyle.wrap(foregroundColorSpan),
                    limit2[0],
                    limit2[1],
                    SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
                )
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        context.toast("协议")
                    }

                }, limit1[0], limit1[1], SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
//                setSpan(object : ClickableSpan() {
//                    override fun onClick(widget: View) {
//                        context.toast("隐私")
//                    }
//
//                }, limit2[0], limit2[1], SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

        tv_agree.setOnClickListener {
            onAgree.invoke()
            dismiss()
        }
        tv_disagree.setOnClickListener { context.finish() }

    }

    override fun onStart() {
        super.onStart()
        val window = window ?: return
        val attributes = window.attributes
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
        attributes.dimAmount = 0.6f
    }
}