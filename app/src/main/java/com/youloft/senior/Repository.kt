package com.youloft.senior

import android.util.Log
import androidx.lifecycle.liveData
import com.youloft.senior.net.NetWork


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/19 18:52
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/19 18:52
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object Repository {
    private const val TAG = "Repository"
    fun getLogin(params:Map<String,String>) = this.fire {
        val projectTree = NetWork.getLogin(params)
        if (projectTree.status == 200) {
            val bannerList = projectTree.data
            Result.success(projectTree)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.status}  msg is ${projectTree.msg}"))
        }
    }
    fun getItem(params:String) = this.fire {
        val projectTree = NetWork.getItem(params)
        if (projectTree.status == 200) {
            val bannerList = projectTree.data
            Result.success(projectTree)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.status}  msg is ${projectTree.msg}"))
        }
    }

    fun <T> fire(block: suspend () -> Result<T>) =
        liveData {
            val result = try {
                block()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                Result.failure<T>(e)
            }
            emit(result)
        }
}