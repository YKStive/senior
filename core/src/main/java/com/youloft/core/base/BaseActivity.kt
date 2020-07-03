package com.youloft.core.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.youloft.core.Analytics
import com.youloft.util.StatusBarUtils

/**
 * @author you
 * @create 2020/6/18
 * @desc activity基类
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        if (useImmerseToolbar()) {
            immerseToolbar()
        }
        if (useDarkStatus()) {
            StatusBarUtils.StatusBarLightMode(this);
        }
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView()
        initData()
    }

    open fun useImmerseToolbar(): Boolean {
        return true
    }

    open fun useDarkStatus(): Boolean {
        return true
    }

    /**
     * 使用沉浸式菜单栏
     * 迁移至CFApp
     */
    private fun immerseToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.requestFeature(Window.FEATURE_NO_TITLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                )
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()

    private var mLoadingDialog: BaseLoadingDialog? = null
    fun showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = BaseLoadingDialog(this)
            mLoadingDialog!!.show()
        }
    }

    fun dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        Analytics.postActivityResume(this)
    }

    override fun onPause() {
        super.onPause()
        Analytics.postActivityPause(this)
    }
}