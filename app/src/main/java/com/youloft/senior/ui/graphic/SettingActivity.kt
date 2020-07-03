package com.youloft.senior.ui.graphic

import android.content.Intent
import android.os.UserManager
import androidx.fragment.app.FragmentActivity
import com.youloft.coolktx.MarketHelper
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.base.URL_PRIVACY
import com.youloft.senior.base.URL_PROTOCOL
import com.youloft.senior.dialog.ConfirmCancelDialog
import com.youloft.senior.web.WebActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * @author you
 * @create 2020/6/30
 * @desc 设置
 */
class SettingActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        common_title.onBack { finish() }
        common_title.setTitle("设置")
        tv_app_update.setOnClickListener {
            MarketHelper.open(App.instance())
        }

        tv_app_praise.setOnClickListener {
            MarketHelper.open(App.instance())
        }

        tv_logout.setOnClickListener {
            ConfirmCancelDialog(this, getString(R.string.confirm_logout), {
                com.youloft.senior.utils.UserManager.instance.loginOut()
                it.dismiss()
            }).show()
        }

        tv_privacy.setOnClickListener {
            WebActivity.start(this, WebActivity.WebSetting().apply {
                isNeedTab = false
                isIs_hide_title = true
                url = URL_PRIVACY
            })
        }
        tv_protocol.setOnClickListener {
            WebActivity.start(this, WebActivity.WebSetting().apply {
                isNeedTab = false
                isIs_hide_title = true
                url = URL_PROTOCOL
            })
        }

    }

    override fun initData() {

    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}