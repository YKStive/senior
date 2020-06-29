package com.youloft.senior.ui.home

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.ui.gif.GifActivity
import com.youloft.senior.widgt.OperateDialog
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class HomeActivity : BaseActivity() {
    var showPublishFlag = true
    var timecount = 3
    private val timeHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what === 100) {
                if (timecount > 0) {
                    timecount--
                    sendEmptyMessageDelayed(100, 1000)
                } else {
                    img_publish.visibility = View.GONE
                    showPublishFlag = false
                }
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_home

    }

    override fun initView() {
        reqPermissions()
        val mainFragment = MainFragment()
        val homeFragment = HomeFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container_home, homeFragment)
            .add(R.id.fl_container_main, mainFragment)
            .commit()



        btn_home.apply {
            isSelected = true
            setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .hide(mainFragment)
                    .show(homeFragment)
                    .commit()
                main_coin_page.visibility = View.VISIBLE

                isSelected = true
                btn_main.isSelected = false
            }

        }


        btn_main.apply {
            setOnClickListener {
                if (showPublishFlag) {
                    timeHandler.sendEmptyMessageDelayed(100, 1000);
                    img_publish.visibility = View.VISIBLE
                }
                supportFragmentManager.beginTransaction()
                    .hide(homeFragment)
                    .show(mainFragment)
                    .commit()
                isSelected = true
                main_coin_page.visibility = View.GONE
                btn_home.isSelected = false
            }

        }

        btn_function.setOnClickListener {
            OperateDialog(this).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        main_coin_page.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (main_coin_page.onBack()) {
            return
        }
        super.onBackPressed()
    }

    override fun initData() {

    }


    private fun reqPermissions() {
        AndPermission.with(this)
            .runtime()
            .permission(
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE
            )
            .onGranted {
            }
            .onDenied {
                finish()
            }
            .start()
    }

}