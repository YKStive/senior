package com.youloft.senior.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.bean.Post
import kotlinx.coroutines.launch

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class HomeViewModel : BaseViewModel() {
    private val _data: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val data: LiveData<MutableList<Post>>
        get() = _data

    fun getData() {
        viewModelScope.launch {
            val mutableListOf = mutableListOf<Post>()
            mutableListOf.add(Post.testData)
            _data.value = mutableListOf
        }
    }
}