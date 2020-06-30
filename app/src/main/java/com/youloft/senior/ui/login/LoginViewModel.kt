package com.youloft.senior.ui.login

import androidx.lifecycle.MutableLiveData
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.Repository


/**
 *
 * @Description:     java类作用描述
 * @Author:         slh
 * @CreateDate:     2020/6/19 19:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/19 19:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class LoginViewModel : BaseViewModel() {
    val data: MutableLiveData<LoginBean> = MutableLiveData()

//    fun getData(params: Map<String, String>) {
//        val value = Repository.getLogin(params).value
//        data.value = if (value != null) value.getOrNull() else null
//    }

//    fun getData(params: Map<String, String>) {
//            launch(
//                {
//                    data.value = LoadState.Loading()
//                    val data1 = async { NetworkService.apiService.getImage() }
//                    val data2 = async { NetworkService.apiService.getImage() }
//                    val data3 = async { NetworkService.apiService.getImage() }
//                    imageData.value = listOf(data1.await(), data2.await(), data3.await()).map {
//                        it.imgurl
//                    }
//                    loadState.value = LoadState.Success()
//                },
//                {
//                    loadState.value = LoadState.Fail(it.message ?: "加载失败")
//                }
//        }
//    }
}