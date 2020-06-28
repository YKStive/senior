package com.youloft.senior.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.net.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/23 13:24
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 13:24
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MainViewModel : BaseViewModel() {
    //结果
    var resultData = MutableLiveData<List<MineDataBean>>()
    fun getData(
        index: Int,
        direction: Int,
        limit: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
//            val listData = ApiHelper.api.getMineList(index, direction, limit)
            val listData = ApiHelper.api.getMineList()
            withContext(Dispatchers.Main){
                ApiHelper.executeResponse(listData, {
                    resultData.value = it
                })
            }

//            withContext(Dispatchers.Main) {
//                ApiHelper.executeResponse(stickers, {
//                    mAdapter.items = it
//                    mAdapter.notifyDataSetChanged()
//                })
//            }
        }
    }


}