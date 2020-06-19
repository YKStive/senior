package com.youloft.senior.base

import android.app.Application
import kotlin.properties.Delegates

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        private var instance: App by Delegates.notNull()
        fun instance() = instance
    }

}