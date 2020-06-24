package com.youloft.senior.cash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import kotlinx.android.synthetic.main.phone_verify_dialog_layout.*

/**
 * @author xll
 * @date 2020/6/24 15:59
 */
internal class PhoneDialog(ctx: Context) : BaseDialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_verify_dialog_layout)
        parent.setOnClickListener { dismiss() }
        cancel.setOnClickListener { dismiss() }
        bind.setOnClickListener {
            //开启绑定手机号界面
            context.startActivity(Intent(context, BindPhoneActivity::class.java))
            dismiss()
        }
    }

}