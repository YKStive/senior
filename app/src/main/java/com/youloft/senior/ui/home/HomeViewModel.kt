package com.youloft.senior.ui.home

import androidx.lifecycle.MutableLiveData
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.utils.Preference

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class HomeViewModel : BaseViewModel() {
    val data: MutableLiveData<String> = MutableLiveData()

    fun getData() {
        data.value = "hello"
    }
}