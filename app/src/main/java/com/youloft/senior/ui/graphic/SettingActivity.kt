package com.youloft.senior.ui.graphic

import com.youloft.coolktx.MarketHelper
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.base.App
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
            //todo  退出登录
        }

    }

    override fun initData() {

    }
}