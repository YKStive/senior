package com.youloft.senior.ui.graphic

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import kotlinx.android.synthetic.main.activity_invite_role.*

/**
 * @author you
 * @create 2020/7/1
 * @desc 好友邀请规则
 */
class InviteRuleActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_invite_role
    }


    override fun initView() {
        common_title.onBack { finish() }
        common_title.setTitle("活动规则")
    }

    override fun initData() {
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, InviteRuleActivity::class.java)
            context.startActivity(intent)
        }
    }
}