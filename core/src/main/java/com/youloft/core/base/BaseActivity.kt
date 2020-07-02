package com.youloft.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.youloft.core.Analytics

/**
 * @author you
 * @create 2020/6/18
 * @desc activity基类
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView()
        initData()
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