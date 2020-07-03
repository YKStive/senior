package com.youloft.core.jump

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

class TempFragment : Fragment() {
    private val code: Int = 1
    private var mResult: ((requestCode: Int, data: Intent?) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun startForResult(intent: Intent, onResult: (requestCode: Int, data: Intent?) -> Unit) {
        this.mResult = onResult
        startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null && !activity!!.isDestroyed) {
            val manager = activity!!.supportFragmentManager
            val targetFragment = manager.findFragmentByTag(TAG)
            targetFragment?.apply {
                manager.beginTransaction()
                    .remove(this)
                    .commit()
            }
        }
        mResult?.invoke(resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val TAG = "TempFragment"
    }

}