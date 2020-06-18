package com.youloft.senior.base

import android.app.Application

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
object App : Application() {

    val context by lazy { this }


    override fun onCreate() {
        super.onCreate()

    }



}