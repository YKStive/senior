package com.youloft.senior.utils.gifmaker

import android.util.Log


internal class TaskTime {


    private var start = 0L

    val MIL_SECOND = 1000F

    init {
        start = System.currentTimeMillis()
    }

    fun release(name: String) {
        val spend = System.currentTimeMillis() - start
        val second = spend / MIL_SECOND
        val result = String.format("方法: %-20s 耗时 %2.6f second\n", name, second)
        Log.e("TaskTime", result)
    }

}