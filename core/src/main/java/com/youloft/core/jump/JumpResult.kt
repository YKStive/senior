package com.youloft.core.jump

import android.content.ComponentName
import android.content.Intent
import androidx.fragment.app.FragmentActivity

class JumpResult(activity: FragmentActivity) {

    private var mActivity: FragmentActivity = activity
    var tempFragment: TempFragment? = null

    init {
        getTempFragment(activity)
    }


    private fun getTempFragment(activity: FragmentActivity) {
        tempFragment = findTempFragment(activity)
        if (tempFragment == null) {
            tempFragment = TempFragment()
            val manager = activity.supportFragmentManager
            manager.beginTransaction()
                .add(tempFragment!!, TempFragment.TAG)
                .commitAllowingStateLoss()
            manager.executePendingTransactions();
        }
    }


    private fun findTempFragment(activity: FragmentActivity): TempFragment? {
        return activity.supportFragmentManager.findFragmentByTag(TempFragment.TAG) as TempFragment?
    }


    fun startForResult(intent: Intent, onResult: (requestCode: Int, data: Intent?) -> Unit) {
        tempFragment!!.startForResult(intent, onResult)
    }

    fun startForResult(clazz: Class<*>, onResult: (requestCode: Int, data: Intent?) -> Unit) {
        val intent = Intent()
        intent.component = ComponentName(mActivity, clazz)
        tempFragment!!.startForResult(intent, onResult)
    }

    public interface Callback {
        var requestCode: Int
        var responseCode: Int
        var data: Intent?
    }
}