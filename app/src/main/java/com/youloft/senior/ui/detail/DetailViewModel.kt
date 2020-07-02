package com.youloft.senior.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/7/2 15:00
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/2 15:00
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class DetailViewModel : BaseViewModel() {
    companion object {
        private const val TAG = "DetailViewModel"
    }

    var addFavorite = MutableLiveData<Int>()
    var addComment = MutableLiveData<Int>()
    fun addFavorite(params: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = ApiHelper.api.parse(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {
                        addFavorite.value = 200
                    }
                }
            } catch (e: Exception) {
                e.toString().logE(TAG)
            }
        }
    }

    fun comment(params: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = ApiHelper.api.commnet(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {
                        addComment.value = 200
                    }
                }
            } catch (e: Exception) {
                e.toString().logE(TAG)
            }
        }
    }

}