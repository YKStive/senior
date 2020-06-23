package com.youloft.senior.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.net.bean.CommenttData
import com.youloft.net.bean.CommnetBean
import com.youloft.net.bean.ItemDetailBean
import com.youloft.senior.bean.Post
import kotlinx.coroutines.launch


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/22 18:06
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/22 18:06
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class FavoriteViewModel : BaseViewModel() {
    private val comment: MutableLiveData<MutableList<CommenttData>> = MutableLiveData()
    val _data: MutableLiveData<MutableList<CommenttData>>
        get() = comment

    fun getData() {
        viewModelScope.launch {

        }
    }
}