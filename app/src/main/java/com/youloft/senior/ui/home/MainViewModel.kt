package com.youloft.senior.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


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
    companion object {
        private const val TAG = "MainViewModel"
    }

    //结果
    var resultData = MutableLiveData<List<MineDataBean>>()

    //结果 异常结果
    var errorStr = MutableLiveData<String>()
    fun getData(
        params: HashMap<String, String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//            val listData = ApiHelper.api.getMineList(index, direction, limit)
                val listData = ApiHelper.api.getMineList(params)
                withContext(Dispatchers.Main) {
                    ApiHelper.executeResponse(listData, {
                        resultData.value = it
                    }, {
                        errorStr.value = it
                    })
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorStr.value = e.message.toString()
                }
                e.message.toString().logE(TAG)
            }

        }
    }


}