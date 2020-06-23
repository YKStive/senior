package com.youloft.senior.coin

import android.app.Instrumentation
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import com.youloft.core.base.BaseDialog
import com.youloft.senior.R
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.layout_invite_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author xll
 * @date 2020/6/23 13:47
 */
internal class InviteDialog(ctx: Context) : BaseDialog(ctx) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_invite_dialog)
        parent.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val inst = Instrumentation()
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        confirm.setOnClickListener {
            if (TextUtils.isEmpty(this.invite_edit.text.toString())) {
                ToastMaster.showLongToast(context, "请输入邀请码")
                return@setOnClickListener
            }
            confirm.isClickable = false
            TaskManager.instance.completeTask(
                "Inviter_code", context, null,
                this.invite_edit.text.toString(), null, {
                    dismiss()
                }, {
                    confirm.isClickable = true
                }
            )
        }

        close.setOnClickListener {
            dismiss()
        }
    }
}