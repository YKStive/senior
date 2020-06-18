package com.youloft.senior.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.youloft.core.base.BaseViewModel

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class MainViewModel : BaseViewModel() {
    val data: MutableLiveData<String> = MutableLiveData()

    fun getData() {
        data.value = "hello"
    }
}