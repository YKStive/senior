package com.youloft.senior.coin

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import kotlinx.android.synthetic.main.coin_tips_dialog_layout.*

/**
 * @author xll
 * @date 2020/6/23 17:47
 */
class CoinTipsDialog(
    ctx: Context,
    val title: String,
    var content: String,
    val coin: Int,
    val cash: String?,
    val button: String?
) : BaseDialog(ctx) {

    var buttonListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coin_tips_dialog_layout)
        bindUI()
    }

    fun bindCoinCash(coin: Int, cash: String?): CoinTipsDialog {
        if (TextUtils.isEmpty(content)) {
            content = "我的金币:" + coin + "≈" + cash.stringToInt() + "元"
        }
        return this
    }

    fun setButtonListener(buttonListener: (() -> Unit)): CoinTipsDialog {
        this.buttonListener = buttonListener
        return this
    }

    private fun bindUI() {
        top_title.text = title
        coin_text.text = if (TextUtils.isEmpty(cash)) "+${coin}" else "+${cash}"
        coin_place.text = if (TextUtils.isEmpty(cash)) "金币" else "元"
        if (title.length > 4) {
            title_bg.setBackgroundResource(R.drawable.theme_jl_pop_gx_title2)
        }
        if (TextUtils.isEmpty(content)) {
        } else {
            content_text.text = content
        }
        if (TextUtils.isEmpty(button)) {
            button_text_group.visibility = View.GONE
        } else {
            if (button.equals("cash-double")) {
                button_text.visibility = View.GONE
                cash_button.visibility = View.VISIBLE
                try {
                    cash_button_text.text = (cash!!.toFloat() * 2).toString()
                } catch (e: Exception) {
                }
            } else {
                button_text.visibility = View.VISIBLE
                cash_button.visibility = View.GONE
                button_text.text = button
            }
        }
        close.setOnClickListener { dismiss() }
        dialog_root.setOnClickListener { dismiss() }
        button_text_group.setOnClickListener {
            if (buttonListener != null) {
                buttonListener!!.invoke()
            }
            dismiss()
        }
    }

}