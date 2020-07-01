package com.youloft.senior.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author xll
 * @date 2020/7/1 17:55
 */
internal class ActivityManager : Application.ActivityLifecycleCallbacks {
    var mActiveActivityCount = 0
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    var topActivity: Activity? = null

    companion object {
        /**
         * 获取实例
         *
         * @return
         */
        /**
         * 实例
         */
        val instance by lazy { ActivityManager() }

    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        mActiveActivityCount--
        if (mActiveActivityCount <= 0) {
            mActiveActivityCount = 0
            topActivity = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mActiveActivityCount++
        topActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        topActivity = activity
    }
}