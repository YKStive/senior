package com.youloft.senior.cash

import android.content.Context
import android.os.Bundle
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import kotlinx.android.synthetic.main.normal_tips_dialog_layout.*

/**
 * @author xll
 * @date 2020/6/24 15:59
 */
internal class NormalTipsDialog(ctx: Context, val speedCallBack: () -> Unit) : BaseDialog(ctx) {
    var money: String = ""
    fun bindMoney(money: String): NormalTipsDialog {
        this.money = money
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cash_tips_dialog_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        cash_value.text = this.money
        cancel.setOnClickListener { dismiss() }
        okay.setOnClickListener {
            //开始加速回调
            speedCallBack.invoke()
            dismiss()
        }
    }

}