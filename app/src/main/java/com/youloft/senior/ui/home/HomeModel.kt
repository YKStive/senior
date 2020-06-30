package com.youloft.senior.ui.home

import androidx.lifecycle.MutableLiveData
import com.youloft.core.base.BaseViewModel


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/30 14:46
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/30 14:46
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class HomeModel:BaseViewModel() {
    var isLogin= MutableLiveData<Boolean>()
}