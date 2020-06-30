package com.youloft.senior.ui.detail

import android.content.Context
import android.os.Bundle
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import kotlinx.android.synthetic.main.cash_tips_dialog_layout.*
import kotlinx.android.synthetic.main.dialog_delete_tips.*

/**
 *
 * @Description:     删除提示弹出
 * @Author:         slh
 * @CreateDate:     2020/6/18 15:19
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/18 15:19
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
internal class DeletePostTipsDialog(ctx: Context, val deleteCallBack: () -> Unit) : BaseDialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete_tips)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        close.setOnClickListener { dismiss() }
        tv_post.setOnClickListener {
            deleteCallBack.invoke()
            dismiss()
        }
    }

}