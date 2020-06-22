package com.youloft.senior.ui.home

import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.ui.gif.GifActivity
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class HomeActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_home

    }

    override fun initView() {
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

                isSelected = true
                btn_main.isSelected = false
            }

        }


        btn_main.apply {
            setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .hide(homeFragment)
                    .show(mainFragment)
                    .commit()
                isSelected = true
                btn_home.isSelected = false
            }

        }

        btn_function.setOnClickListener {
            GifActivity.start(this)
        }
    }

    override fun initData() {
    }
}