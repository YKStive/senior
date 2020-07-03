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
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.ui.login.LoginFragment
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.UserManager
import com.youloft.senior.widgt.OperateDialog
import com.youloft.senior.widgt.PrivacyDialog
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 首页
 */
class HomeActivity : BaseVMActivity() {

    private var isAgreePrivacy by Preference(Preference.IS_AGREE_PRIVACY, false)
    lateinit var mViewModel: HomeModel
    private val mainFragment = MainFragment()
    private val homeFragment = HomeFragment()
    private val notLoginFragment = LoginFragment()
    override fun getLayoutResId(): Int {
        return R.layout.activity_home

    }

    private var showPublish = true
    override fun initView() {
        if (!isAgreePrivacy) {
            PrivacyDialog(this) {
                isAgreePrivacy = true
                reqPermissions()
            }.show()
        } else {
            reqPermissions()
        }


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
                img_publish.apply {
                    if (visibility == View.GONE && showPublish) {
                        showPublish = false
                        visibility = View.VISIBLE
                        postDelayed({
                            visibility = View.GONE
                        }, 3000)
                    }
                }
//                if (com.youloft.senior.utils.UserManager.instance.hasLogin()) {
                supportFragmentManager.beginTransaction()
                    .hide(homeFragment)
                    .hide(notLoginFragment)
                    .show(mainFragment)
                    .commit()
//                } else {
//                    supportFragmentManager.beginTransaction()
//                        .hide(homeFragment)
//                        .hide(mainFragment)
//                        .show(notLoginFragment)
//                        .commit()
//                }
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
            LoginDialog.tryLogin(this) {
                OperateDialog(this).show()
            }
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
                supportFragmentManager.beginTransaction()
                    .add(R.id.fl_container_home, homeFragment)
                    .add(R.id.fl_container_main, mainFragment)
                    .add(R.id.fl_container_main, notLoginFragment)
                    .commit()
            }
            .onDenied {
                finish()
            }
            .start()
    }

}