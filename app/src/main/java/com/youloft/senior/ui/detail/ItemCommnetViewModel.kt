package com.youloft.senior.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.net.bean.CommentBean
import com.youloft.senior.net.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
class ItemCommnetViewModel : BaseViewModel() {

    //结果
    var resultData = MutableLiveData<List<CommentBean>>()
    fun getData(
        params: Map<String, String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
//            val listData = ApiHelper.api.getMineList(index, direction, limit)
            val listData = ApiHelper.api.getCommentList(params)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(listData, {
                    resultData.value = it
                })
            }

        }
    }
}
