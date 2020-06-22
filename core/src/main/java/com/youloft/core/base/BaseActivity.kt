package com.youloft.core.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

//     fun showShortToast(content: String) {
//        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
//    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()
}