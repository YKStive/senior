package com.youloft.senior.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.net.bean.ItemDetailBean
import com.youloft.senior.bean.Post
import kotlinx.coroutines.launch
import com.youloft.senior.Repository as Repository


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/23 18:47
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 18:47
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MovieViewModel : BaseViewModel() {
//    val movieItemData = MutableLiveData<ItemDetailBean>()
//    fun getData(id: String) {
//        viewModelScope.launch {
//            val value = Repository.getItem(id).value
//            movieItemData.value = if (value != null) value.getOrNull() else null
//        }
//    }


    private val item = MutableLiveData<String>()

    val itemData = Transformations.switchMap(item) { id ->
        Repository.getItem(id)
    }

    fun getItem(id: String) {
        item.value = id
    }
}