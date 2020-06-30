package com.youloft.senior.ui.home

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.ui.login.LoginFragment
import com.youloft.senior.widgt.OperateDialog
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class HomeActivity : BaseVMActivity() {
    companion object{
        private const val COUnt_DOWN_CODE = 100
    }
    lateinit var mViewModel: HomeModel
    private val mainFragment = MainFragment()
    private val homeFragment = HomeFragment()
    private val notLoginFragment = LoginFragment()
    var showPublishFlag = true
    var timecount = 3
    private val timeHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what === COUnt_DOWN_CODE) {
                if (timecount > 0) {
                    timecount--
                    sendEmptyMessageDelayed(COUnt_DOWN_CODE, 1000)
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
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container_home, homeFragment)
            .add(R.id.fl_container_main, mainFragment)
            .add(R.id.fl_container_main, notLoginFragment)
            .commit()



        btn_home.apply {
            isSelected = true
            setOnClickListener {
                if (img_publish.visibility == View.VISIBLE) {
                    img_publish.visibility = View.GONE
                    showPublishFlag = false
                    timeHandler.removeMessages(COUnt_DOWN_CODE)
                }
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
                    timeHandler.sendEmptyMessageDelayed(COUnt_DOWN_CODE, 1000);
                    img_publish.visibility = View.VISIBLE
                }
                if (com.youloft.senior.utils.UserManager.instance.hasLogin()) {
                    supportFragmentManager.beginTransaction()
                        .hide(homeFragment)
                        .hide(notLoginFragment)
                        .show(mainFragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .hide(homeFragment)
                        .hide(mainFragment)
                        .show(notLoginFragment)
                        .commit()
                }
                isSelected = true
                main_coin_page.visibility = View.GONE
                btn_home.isSelected = false
            }

        }

        btn_function.setOnClickListener {
            OperateDialog(this).show()
        }
        img_publish.setOnClickListener {
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

    /**
     * 只针对“我的”登录成功后的页面切换
     */
    override fun startObserve() {
        mViewModel = ViewModelProvider(this).get(HomeModel::class.java)
        mViewModel.isLogin.observe(this, Observer { isLogin ->
            if (isLogin) {
                supportFragmentManager.beginTransaction()
                    .hide(homeFragment)
                    .hide(notLoginFragment)
                    .show(mainFragment)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .hide(homeFragment)
                    .hide(mainFragment)
                    .show(notLoginFragment)
                    .commit()
            }
        })
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